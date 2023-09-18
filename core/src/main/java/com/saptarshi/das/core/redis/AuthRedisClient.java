package com.saptarshi.das.core.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saptarshi.das.core.models.User;
import lombok.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import redis.clients.jedis.JedisPooled;

import java.util.stream.Collectors;

public class AuthRedisClient implements RedisClient {
    private static final long EXPIRATION = 60 * 60;
    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final JedisPooled redis;

    public AuthRedisClient(final String host, final Integer port) {
        redis = new JedisPooled(host, port);
    }

    @Override
    public void setUserDetailsAndToken(@NonNull final UserDetails userDetails,
                                       @NonNull final String token) throws JsonProcessingException {
        final RedisUser user = RedisUser.builder()
                .email(userDetails.getUsername())
                .password(userDetails.getPassword())
                .authorities(userDetails.getAuthorities().stream()
                        .map(String::valueOf)
                        .collect(Collectors.toList())
                )
                .build();
        redis.setex(user.getEmail(), EXPIRATION, token);
        redis.setex(token, EXPIRATION, mapper.writeValueAsString(user));
    }

    @Override
    public UserDetails getUserDetailsFromToken(@NonNull String token) throws JsonProcessingException {
        final String value = redis.get(token);
        System.out.println("Redis Key: " + token + ", Value: " + value);
        final RedisUser user = mapper.readValue(value, new TypeReference<RedisUser>() {});

        return User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getAuthorities().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
                )
                .build();
    }
}
