package com.saptarshi.das.admin.responses;

import com.saptarshi.das.admin.models.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
public class VerifiedUserResponse {
    private UserDetails userDetails;
}
