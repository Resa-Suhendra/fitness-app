package com.resa.fitness.controller;

import com.resa.fitness.model.*;
import com.resa.fitness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.resa.fitness.entity.User;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(
            path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> register(@RequestBody RegisterUserRequest request) {
        userService.register(request);
        return WebResponse.<String>builder().data("Email konfirmasi telah dikirim silahkan cek email anda.").build();
    }

    @GetMapping(
            path = "/api/users/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> get(User user) {
        UserResponse userResponse = userService.get(user);
        return WebResponse.<UserResponse>builder().data(userResponse).build();
    }

    @PatchMapping(
            path = "/api/users/current",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> update(User user, @RequestBody UpdateUserRequest request) {
        UserResponse userResponse = userService.update(user, request);
        return WebResponse.<UserResponse>builder().data(userResponse).build();
    }

    @GetMapping(
            path = "/api/users/status",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> checkStatus(@RequestParam String email) {
        String status = userService.checkStatus(email);
        return WebResponse.<String>builder().data(status).build();
    }


    @PutMapping(
            path = "/api/users/card-info",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> updateCard(User user, @RequestBody UpdateCardRequest newCardInfo) {
        userService.updateCreditCardInfo(user, newCardInfo.toString());
        return WebResponse.<String>builder().data("OK").build();
    }


    @GetMapping(
            path = "/api/users/send-verification",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> sendVerification(@RequestParam String email) {
        userService.sendEmailVerification(email);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "/api/users/verify",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> verify(@RequestParam String email, @RequestParam String token) {
        userService.verify(email, token);
        return WebResponse.<String>builder().data("OK").build();
    }

}
