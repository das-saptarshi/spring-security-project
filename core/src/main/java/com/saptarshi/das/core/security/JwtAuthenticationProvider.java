package com.saptarshi.das.core.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.saptarshi.das.core.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private RedisClient redis;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(
            String username,
            UsernamePasswordAuthenticationToken authentication
    ) throws AuthenticationException {
        try {
            final String token = authentication.getPrincipal().toString();
            System.out.println("JWT: " + token);
            return redis.getUserDetailsFromToken(token);
        } catch (JsonProcessingException e) {
            throw new AuthenticationException("Invalid Token/User.") {};
        }
    }
}
