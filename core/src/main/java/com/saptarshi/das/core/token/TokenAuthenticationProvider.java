package com.saptarshi.das.core.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saptarshi.das.core.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private static final String DOT_REGEX = "\\.";
    private static final int PAYLOAD_INDEX = 1;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Base64.Decoder DECODER = Base64.getUrlDecoder();

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // No logic need
    }

    @Override
    protected UserDetails retrieveUser(String username, 
                                       UsernamePasswordAuthenticationToken authentication
    ) throws AuthenticationException {
        log.info("In Token Based Authentication");
        final String token = authentication.getPrincipal().toString();
        final String[] tokenParts = token.split(DOT_REGEX);
        final String encodedPayload = tokenParts[PAYLOAD_INDEX];
        final String payload = new String(DECODER.decode(encodedPayload));

        try {
            return getUserDetailsFromPayload(payload);
        } catch (JsonProcessingException e) {
            throw new AuthenticationException("Invalid Token") {};
        }
    }

    private UserDetails getUserDetailsFromPayload(final String payload) throws JsonProcessingException {
        final JsonNode userDetails = MAPPER.readTree(payload);
        final String email = userDetails.get("email").toString();
        final String password = userDetails.get("password").toString();
        final List<SimpleGrantedAuthority> authorities = MAPPER.readValue(userDetails.get("authorities").toString(),
                new TypeReference<ArrayList<SimpleGrantedAuthority>>() {});

        return User.builder()
                .email(email)
                .password(password)
                .authorities(authorities)
                .build();
    }
}
