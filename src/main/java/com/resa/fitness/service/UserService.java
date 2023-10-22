package com.resa.fitness.service;

import com.resa.fitness.security.BCrypt;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.resa.fitness.entity.User;
import com.resa.fitness.model.RegisterUserRequest;
import com.resa.fitness.model.UpdateUserRequest;
import com.resa.fitness.model.UserResponse;
import com.resa.fitness.repository.UserRepository;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void register(RegisterUserRequest request) {
        validationService.validate(request);

        if (userRepository.existsById(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setCreditCardInfo(encryptCreditCardInfo(request.getCreditCardInfo()));
        user.setVerified(false);

        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiredAt(next2hours());

        userRepository.save(user);
        sendEmailVerification(request.getEmail());
    }

    public void sendEmailVerification(String email) {
        User user = userRepository.findById(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user_not_found"));
        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiredAt(next2hours());
        userRepository.save(user);
        try {
            emailService.sendEmail(user.getEmail(), "Email Confirmation", "Please click this url bellow to confirm your email registration  :\n http://localhost:8080/api/users/verify?email=" + user.getEmail()+ "&token=" + user.getToken());
        } catch (MessagingException e) {

        }
    }

    @Transactional
    public void verify(String email, String token) {
        User user = userRepository.findById(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user_not_found"));
        if (user.getToken().equals(token)) {
            if (user.getTokenExpiredAt() < System.currentTimeMillis()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired");
            }
            user.setVerified(true);
            user.setToken(null);
            user.setTokenExpiredAt(null);
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
        }
    }


    @Transactional
    public UserResponse update(User user, UpdateUserRequest request) {
        validationService.validate(request);

        log.info("REQUEST : {}", request);

        if (Objects.nonNull(request.getName())) {
            user.setName(request.getName());
        }
        userRepository.save(user);

        log.info("USER : {}", user.getName());

        return UserResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public void updateCreditCardInfo(User user, String newCreditCardInfo) {
            String encryptedCreditCardInfo = encryptCreditCardInfo(newCreditCardInfo);
            user.setCreditCardInfo(encryptedCreditCardInfo);
            userRepository.save(user);
    }


    private String encryptCreditCardInfo(String creditCardInfo) {
        try {
            SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedData = cipher.doFinal(creditCardInfo.getBytes());

            String encryptedCreditCardInfo = Base64.getEncoder().encodeToString(encryptedData);

            return encryptedCreditCardInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserResponse get(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public String checkStatus(String email) {
        final User user = userRepository.findById(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UNREGISTERED"));
        return user.isVerified() ? "VERIFIED" : "UNVERIFIED";
    }

    private Long next2hours() {
        return System.currentTimeMillis() + 2 * 60 * 60 * 1000;
    }
}
