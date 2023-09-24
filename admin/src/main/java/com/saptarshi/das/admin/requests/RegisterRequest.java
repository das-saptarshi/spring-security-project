package com.saptarshi.das.admin.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "First Name is required")
    private String firstName;
    private String lastName;
    @NotBlank(message = "Email is required")
    @Email(message = "Email provided is not valid")
    private String email;
    @NotBlank(message = "Password is required")
    @Length(min = 8, max = 30, message = "Length of Password must be in the range of 8 to 30")
    private String password;
}
