package com.resa.fitness.service;

import com.resa.fitness.model.ChangePasswordRequest;
import com.resa.fitness.security.BCrypt;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.resa.fitness.entity.User;
import com.resa.fitness.model.LoginUserRequest;
import com.resa.fitness.model.TokenResponse;
import com.resa.fitness.repository.UserRepository;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    EmailService emailService;

    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        validationService.validate(request);

        User user = userRepository.findById(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password wrong"));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(next7Days());
            userRepository.save(user);

            if (!user.isVerified()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email not verified, please verify your email");
            }

            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password wrong");
        }
    }

    @Transactional
    public void logout(User user) {
        user.setToken(null);
        user.setTokenExpiredAt(null);

        userRepository.save(user);
    }

    @Transactional
    public TokenResponse refreshToken(User user) {
        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiredAt(next7Days());

        userRepository.save(user);

        return TokenResponse.builder()
                .token(user.getToken())
                .expiredAt(user.getTokenExpiredAt())
                .build();
    }


    @Transactional
    public void sendEmailResetPassword(String email) {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email not found"));

        String token = UUID.randomUUID().toString();
        user.setToken(token);
        user.setTokenExpiredAt(next7Days());
        userRepository.save(user);

        try {
            emailService.sendEmail(email, "Reset Password", "Click this link to reset your password: http://localhost:8080/api/auth/reset-password?token=" + token);
        } catch (MessagingException e) {

        }
    }

    @Transactional
    public void resetPassword(String email, String password) {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email not found"));

        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        userRepository.save(user);
    }


    @Transactional
    public void changePassword(User user, ChangePasswordRequest request) {
        if (BCrypt.checkpw(request.getOldPassword(), user.getPassword())) {
            user.setPassword(BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt()));
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password wrong");
        }
    }

    private Long next7Days() {
        return System.currentTimeMillis() + (1000 * 60 * 24 * 7);
    }

}
