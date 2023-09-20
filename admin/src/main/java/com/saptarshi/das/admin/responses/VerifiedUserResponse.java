package com.saptarshi.das.admin.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifiedUserResponse {
    private String username;
    private String password;
    private List<String> authorities;
}
