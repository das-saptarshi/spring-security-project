package com.saptarshi.das.core.security.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.saptarshi.das.core.constants.ExceptionConstants.INVALID_TOKEN_MESSAGE;
import static com.saptarshi.das.core.constants.SecurityConstants.AUTHORITIES_KEY;
import static com.saptarshi.das.core.constants.SecurityConstants.PASSWORD_KEY;
import static com.saptarshi.das.core.constants.SecurityConstants.USERNAME_KEY;

public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private static final String DOT_REGEX = "\\.";
    private static final int PAYLOAD_INDEX = 1;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Base64.Decoder DECODER = Base64.getUrlDecoder();

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication
    ) throws AuthenticationException {}

    @Override
    protected UserDetails retrieveUser(String username, 
                                       UsernamePasswordAuthenticationToken authentication
    ) throws AuthenticationException {
        final String token = authentication.getPrincipal().toString();
        final String[] sections = token.split(DOT_REGEX);
        final String encodedPayload = sections[PAYLOAD_INDEX];
        final String payload = new String(DECODER.decode(encodedPayload));

        try {
            return getUserDetailsFromPayload(payload);
        } catch (JsonProcessingException e) {
            throw new AuthenticationException(INVALID_TOKEN_MESSAGE) {};
        }
    }

    private UserDetails getUserDetailsFromPayload(final String payload) throws JsonProcessingException {
        final JsonNode userDetails = MAPPER.readTree(payload);
        final String username = userDetails.get(USERNAME_KEY).toString();
        final String password = userDetails.get(PASSWORD_KEY).toString();
        final List<SimpleGrantedAuthority> authorities = MAPPER.readValue(userDetails.get(AUTHORITIES_KEY).toString(),
                new TypeReference<ArrayList<SimpleGrantedAuthority>>() {});

        return User.builder()
                .username(username)
                .password(password)
                .authorities(authorities)
                .build();
    }
}
