package com.resa.fitness.controller;

import com.resa.fitness.model.ChangePasswordRequest;
import com.resa.fitness.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.resa.fitness.entity.User;
import com.resa.fitness.model.LoginUserRequest;
import com.resa.fitness.model.TokenResponse;
import com.resa.fitness.model.WebResponse;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(
            path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse> login(@RequestBody LoginUserRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        return WebResponse.<TokenResponse>builder().data(tokenResponse).build();
    }

    @DeleteMapping(
            path = "/api/auth/logout",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> logout(User user) {
        authService.logout(user);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "/api/auth/refresh-token",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse> refreshToken(User user) {
        TokenResponse newTokenResponse = authService.refreshToken(user);
        return WebResponse.<TokenResponse>builder().data(newTokenResponse).build();
    }

    @GetMapping(
            path = "/api/auth/reset-password",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> resetPassword(@RequestParam String email) {
        authService.sendEmailResetPassword(email);
        return WebResponse.<String>builder().data("OK").build();
    }

    @PutMapping(
            path = "/api/auth/reset-password",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        authService.resetPassword(token, newPassword);
        return WebResponse.<String>builder().data("OK").build();
    }

    @PatchMapping(
            path = "/api/auth/change-password",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> changePassword(User user, @RequestBody ChangePasswordRequest request) {
        authService.changePassword(user, request);
        return WebResponse.<String>builder().data("OK").build();
    }
}
