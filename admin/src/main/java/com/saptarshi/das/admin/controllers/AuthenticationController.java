package com.saptarshi.das.admin.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.saptarshi.das.admin.exceptions.UserAlreadyExistsException;
import com.saptarshi.das.admin.requests.AuthenticationRequest;
import com.saptarshi.das.admin.requests.RegisterRequest;
import com.saptarshi.das.admin.responses.AuthenticationResponse;
import com.saptarshi.das.admin.services.AuthenticationService;
import com.saptarshi.das.admin.responses.RegistrationResponse;
import com.saptarshi.das.core.requests.VerifyTokenRequest;
import com.saptarshi.das.core.responses.VerifiedUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public RegistrationResponse register(
            @RequestBody RegisterRequest request
    ) throws UserAlreadyExistsException {
        return authenticationService.register(request);
    }

    @PostMapping("/generate-token")
    public AuthenticationResponse generateToken(
            @RequestBody AuthenticationRequest request
    ) throws JsonProcessingException {
        return authenticationService.generateToken(request);
    }

    @PostMapping("/verify-token")
    public VerifiedUserResponse verifyToken(
            @RequestBody final VerifyTokenRequest request
    ) {
        return authenticationService.verifyToken(request.getToken());
    }
}
