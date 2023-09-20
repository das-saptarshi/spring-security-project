package com.saptarshi.das.admin.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.saptarshi.das.admin.exceptions.UserAlreadyExistsException;
import com.saptarshi.das.admin.models.Role;
import com.saptarshi.das.admin.models.UserEntity;
import com.saptarshi.das.admin.repositories.UserRepository;
import com.saptarshi.das.admin.requests.AuthenticationRequest;
import com.saptarshi.das.admin.requests.RegisterRequest;
import com.saptarshi.das.admin.responses.AuthenticationResponse;
import com.saptarshi.das.admin.responses.RegistrationResponse;
import com.saptarshi.das.admin.responses.VerifiedUserResponse;
import com.saptarshi.das.core.security.redis.RedisClient;
import com.saptarshi.das.core.security.redis.TokenNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.saptarshi.das.admin.constants.ApplicationConstants.USER_REGISTRATION_SUCCESS_MESSAGE;
import static com.saptarshi.das.admin.constants.ExceptionConstants.INVALID_TOKEN;
import static com.saptarshi.das.admin.constants.ExceptionConstants.USER_ALREADY_EXISTS_MESSAGE;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RedisClient redis;

    public RegistrationResponse register(final RegisterRequest request) throws UserAlreadyExistsException {
        final Optional<UserEntity> existingUser = userRepository.findByUsername(request.getUsername());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(USER_ALREADY_EXISTS_MESSAGE);
        }

        final UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_VIEWER)
                .build();

        userRepository.save(user);

        return RegistrationResponse.builder()
                .message(USER_REGISTRATION_SUCCESS_MESSAGE)
                .build();
    }


    public AuthenticationResponse generateToken(final AuthenticationRequest request) throws JsonProcessingException {
        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );

        authenticationManager.authenticate(authToken);

        final Optional<UserEntity> user = userRepository.findByUsername(request.getUsername());
        final String token = jwtService.generateToken(user.get());
        redis.setUserDetailsAndToken(user.get(), token);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    public VerifiedUserResponse verifyToken(@NonNull final String token) {
        UserDetails userDetails;
        try {
            userDetails = redis.getUserDetailsFromToken(token);
        } catch (JsonProcessingException | TokenNotFoundException e) {
            throw new AccessDeniedException(INVALID_TOKEN);
        }

        return VerifiedUserResponse.builder()
                .username(userDetails.getUsername())
                .password(userDetails.getPassword())
                .authorities(userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .build();
    }
}
