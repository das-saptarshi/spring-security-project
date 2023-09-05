package com.saptarshi.das.core.responses;

import com.saptarshi.das.core.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifiedUserResponse {
    private String email;
    private String password;
    private List<String> authorities;
}
