//package com.saptarshi.das.admin.filters;
//
//import com.saptarshi.das.admin.services.JwtService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//    private final JwtService jwtService;
//    private final UserDetailsService userDetailsService;
//
//    @Override
//    protected void doFilterInternal(
//            @NonNull HttpServletRequest request,
//            @NonNull HttpServletResponse response,
//            @NonNull FilterChain filterChain
//    ) throws ServletException, IOException {
//        final String bearerToken = request.getHeader("Authorization");
//        if (StringUtils.isBlank(bearerToken) || !bearerToken.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        final String jwtToken = bearerToken.substring(7);
//        validateTokenAndUpdateSecurity(jwtToken, request);
//
//        filterChain.doFilter(request, response);
//    }
//
//    private void validateTokenAndUpdateSecurity(final String token,
//                                                final HttpServletRequest request) {
//        final String username = jwtService.extractUsername(token);
//        if (StringUtils.isBlank(username) ||
//                (SecurityContextHolder.getContext().getAuthentication() != null)) {
//            return;
//        }
//
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//        if (! jwtService.isValidToken(token, userDetails)) {
//            return;
//        }
//
//        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                userDetails,
//                null,
//                userDetails.getAuthorities()
//        );
//
//        authToken.setDetails(
//                new WebAuthenticationDetailsSource().buildDetails(request)
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authToken);
//    }
//}
