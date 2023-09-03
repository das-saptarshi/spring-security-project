package com.saptarshi.das.admin.services;

import com.saptarshi.das.admin.exceptions.UserAlreadyExistsException;
import com.saptarshi.das.admin.models.Role;
import com.saptarshi.das.admin.models.User;
import com.saptarshi.das.admin.repositories.UserRepository;
import com.saptarshi.das.admin.requests.AuthenticationRequest;
import com.saptarshi.das.admin.requests.RegisterRequest;
import com.saptarshi.das.admin.responses.AuthenticationResponse;
import com.saptarshi.das.admin.responses.RegistrationResponse;
import com.saptarshi.das.admin.responses.VerifiedUserResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public RegistrationResponse register(final RegisterRequest request) throws UserAlreadyExistsException {
        final Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User Already Exists!");
        }

        final User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_ADMIN)
                .build();

        userRepository.save(user);

        return RegistrationResponse.builder()
                .message("User Registered Successfully.")
                .build();
    }


    public AuthenticationResponse generateToken(AuthenticationRequest request) {
        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );

        authenticationManager.authenticate(authToken);

        final Optional<User> user = userRepository.findByEmail(request.getEmail());
        final String token = jwtService.generateToken(user.get());
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    public VerifiedUserResponse verifyToken(@NonNull final String token) {
        final String username = jwtService.extractUsername(token);
        if (StringUtils.isBlank(username)) {
            throw new AccessDeniedException("Access Denied! No Token Found.");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (! jwtService.isValidToken(token, userDetails)) {
            throw new AccessDeniedException("Access Denied! Invalid Token.");
        }

        return VerifiedUserResponse.builder()
                .userDetails(userDetails)
                .build();
    }
}
