package com.saptarshi.das.core.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.userdetails.UserDetails;

public interface RedisClient {
    void setUserDetailsAndToken(UserDetails userDetails, String token) throws JsonProcessingException;

    UserDetails getUserDetailsFromToken(String token) throws JsonProcessingException, TokenNotFoundException;
}

