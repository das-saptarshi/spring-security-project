package com.saptarshi.das.core.security;

import com.saptarshi.das.core.requests.VerifyTokenRequest;
import com.saptarshi.das.core.responses.VerifiedUserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private static final RestTemplate restTemplate = new RestTemplate();

    @Value("${service.admin-url}")
    private String adminUrl;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(
            String username,
            UsernamePasswordAuthenticationToken authentication
    ) throws AuthenticationException {

        final VerifyTokenRequest requestBody = VerifyTokenRequest.builder()
                .token(authentication.getName())
                .build();
        final ResponseEntity<VerifiedUserResponse> response = restTemplate
                .postForEntity(adminUrl, requestBody, VerifiedUserResponse.class);

        System.out.println(response);
        if (! HttpStatus.OK.equals(response.getStatusCode())) {
            throw new AuthenticationException("Invalid Token/User.") {};
        }

        return Objects.requireNonNull(response.getBody()).getUserDetails();
    }
}
