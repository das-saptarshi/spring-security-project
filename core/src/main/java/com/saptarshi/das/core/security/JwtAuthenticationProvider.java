package com.saptarshi.das.core.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saptarshi.das.core.models.User;
import com.saptarshi.das.core.requests.VerifyTokenRequest;
import com.saptarshi.das.core.responses.VerifiedUserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper MAPPER = new ObjectMapper();

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
                .token(authentication.getPrincipal().toString())
                .build();

        final ResponseEntity<String> response = restTemplate
                .postForEntity(adminUrl, requestBody, String.class);

        if (! HttpStatus.OK.equals(response.getStatusCode())) {
            throw new AuthenticationException("Invalid Token/User.") {};
        }

        try {
            final VerifiedUserResponse userDetails = MAPPER
                    .readValue(response.getBody(), VerifiedUserResponse.class);
            return User.builder()
                    .email(userDetails.getEmail())
                    .password(userDetails.getPassword())
                    .authorities(userDetails.getAuthorities().stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList())
                    )
                    .build();
        } catch (JsonProcessingException e) {
            throw new AuthenticationException("Invalid Token/User.") {};
        }
    }
}
