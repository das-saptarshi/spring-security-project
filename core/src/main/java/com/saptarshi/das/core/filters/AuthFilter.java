package com.saptarshi.das.core.filters;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

import static com.saptarshi.das.core.constants.ExceptionConstants.INVALID_TOKEN_MESSAGE;

public class AuthFilter extends AbstractAuthenticationProcessingFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final int TOKEN_START_INDEX = BEARER.length();

    public AuthFilter(RequestMatcher requiresAuthenticationRequestMatcher,
                      AuthenticationManager authenticationManager) {
        super(requiresAuthenticationRequestMatcher, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException, IOException, ServletException {
        final String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.isBlank(bearerToken) || !bearerToken.startsWith(BEARER)) {
            throw new AuthenticationException(INVALID_TOKEN_MESSAGE) {};
        }

        final String jwtToken = bearerToken.substring(TOKEN_START_INDEX);
        final UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(jwtToken, jwtToken);

        return this.getAuthenticationManager().authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}
