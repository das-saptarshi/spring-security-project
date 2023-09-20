package com.saptarshi.das.core.security.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisUser {
    private String email;
    private String password;
    private List<String> authorities;
}
