package com.saptarshi.das.core.responses;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
public class VerifiedUserResponse {
    private UserDetails userDetails;
}
