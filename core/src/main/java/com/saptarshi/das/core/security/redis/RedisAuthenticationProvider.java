package com.saptarshi.das.core.security.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import static com.saptarshi.das.core.constants.ExceptionConstants.INVALID_TOKEN_MESSAGE;

@Slf4j
public class RedisAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private final RedisClient redis;

    public RedisAuthenticationProvider(final RedisClient redis) {
        this.redis = redis;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication
    ) throws AuthenticationException {}

    @Override
    protected UserDetails retrieveUser(
            String username,
            UsernamePasswordAuthenticationToken authentication
    ) throws AuthenticationException {
        log.info("In Redis Based Authentication."); //TODO: remove after debugging
        try {
            final String token = authentication.getPrincipal().toString();
            return redis.getUserDetailsFromToken(token);
        } catch (JsonProcessingException | TokenNotFoundException e) {
            throw new AuthenticationException(INVALID_TOKEN_MESSAGE) {};
        }
    }
}
