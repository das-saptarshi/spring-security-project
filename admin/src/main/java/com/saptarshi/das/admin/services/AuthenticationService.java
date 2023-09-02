package com.saptarshi.das.admin.services;

import com.saptarshi.das.admin.exceptions.UserAlreadyExistsException;
import com.saptarshi.das.admin.exceptions.UserNotFoundException;
import com.saptarshi.das.admin.models.Role;
import com.saptarshi.das.admin.models.User;
import com.saptarshi.das.admin.repositories.UserRepository;
import com.saptarshi.das.admin.requests.AuthenticationRequest;
import com.saptarshi.das.admin.requests.RegisterRequest;
import com.saptarshi.das.admin.responses.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public RegistrationResponse register(final RegisterRequest request) throws UserAlreadyExistsException {
        final User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        if (userRepository.exists(Example.of(user))) {
            throw new UserAlreadyExistsException("User Already Exists!");
        }

        userRepository.save(user);

        return RegistrationResponse.builder()
                .message("User Registered Successfully.")
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) throws UserNotFoundException {
        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );

        authenticationManager.authenticate(authToken);
        final Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isEmpty()) {
            throw new UserNotFoundException("User Not Found");
        }

        final String token = jwtService.generateToken(user.get());
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}
