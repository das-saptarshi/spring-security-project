package com.saptarshi.das.core.security.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import redis.clients.jedis.JedisPooled;

import java.util.stream.Collectors;

import static com.saptarshi.das.core.constants.ExceptionConstants.INVALID_TOKEN_MESSAGE;
import static com.saptarshi.das.core.constants.SecurityConstants.EXPIRATION_DURATION_IN_MILLISECONDS;

public class RedisAuthClient implements RedisClient {
    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final JedisPooled redis;

    public RedisAuthClient(final String host, final Integer port) {
        redis = new JedisPooled(host, port);
    }

    @Override
    public void setUserDetailsAndToken(@NonNull final UserDetails userDetails,
                                       @NonNull final String token) throws JsonProcessingException {
        final RedisUser user = RedisUser.builder()
                .username(userDetails.getUsername())
                .password(userDetails.getPassword())
                .authorities(userDetails.getAuthorities().stream()
                        .map(String::valueOf)
                        .collect(Collectors.toList())
                )
                .build();
        if (redis.exists(user.getUsername())) {
            final String existingToken = redis.get(user.getUsername());
            redis.del(existingToken, user.getUsername());
        }
        redis.psetex(user.getUsername(), EXPIRATION_DURATION_IN_MILLISECONDS, token);
        redis.psetex(token, EXPIRATION_DURATION_IN_MILLISECONDS, mapper.writeValueAsString(user));
    }

    @Override
    public UserDetails getUserDetailsFromToken(@NonNull String token)
            throws JsonProcessingException, TokenNotFoundException {
        if (! redis.exists(token)) {
            throw new TokenNotFoundException(INVALID_TOKEN_MESSAGE);
        }
        final String value = redis.get(token);
        final RedisUser user = mapper.readValue(value, new TypeReference<>() {});

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()))
                .build();
    }
}
