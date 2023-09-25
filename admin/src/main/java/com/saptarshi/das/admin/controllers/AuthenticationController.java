package com.saptarshi.das.admin.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.saptarshi.das.admin.exceptions.UserAlreadyExistsException;
import com.saptarshi.das.admin.requests.AuthenticationRequest;
import com.saptarshi.das.admin.requests.RegisterRequest;
import com.saptarshi.das.admin.responses.AuthenticationResponse;
import com.saptarshi.das.admin.responses.RegistrationResponse;
import com.saptarshi.das.admin.responses.VerifiedUserResponse;
import com.saptarshi.das.admin.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    @PostMapping("/user/register")
    public RegistrationResponse registerUser(
            @Valid @RequestBody final RegisterRequest request
    ) throws UserAlreadyExistsException {
        return authenticationService.register(request);
    }

    @PostMapping("/token/generate")
    public AuthenticationResponse generateToken(
            @Valid @RequestBody final AuthenticationRequest request
    ) throws JsonProcessingException {
        return authenticationService.generateToken(request);
    }

    @PostMapping("/token/verify")
    public VerifiedUserResponse verifyToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String token
    ) {
        return authenticationService.verifyToken(token);
    }
}
