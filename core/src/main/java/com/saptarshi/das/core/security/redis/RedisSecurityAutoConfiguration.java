package com.saptarshi.das.core.security.redis;

import com.saptarshi.das.core.filters.AuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@AutoConfiguration
@EnableWebSecurity
@EnableMethodSecurity
@ConditionalOnProperty(
        prefix = "security",
        name = "type",
        havingValue = "redis"
)
public class RedisSecurityAutoConfiguration {

    private static final RequestMatcher PROTECTED_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/**")
    );

    @Value("${security.redis.host}")
    private String host;

    @Value("${security.redis.port}")
    private Integer port;

    @Bean
    public SecurityFilterChain getSecurityFilterChain(final HttpSecurity security,
                                                      final AuthFilter authFilter) throws Exception {

        security
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(PROTECTED_URLS).authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return security.build();
    }

    private RedisClient getRedisClient() {
        return new RedisAuthClient(host, port);
    }


    private AuthenticationProvider getAuthenticationProvider() {
        return new RedisAuthenticationProvider(getRedisClient());
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(final HttpSecurity security) throws Exception {
        return security.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(getAuthenticationProvider())
                .build();
    }

    @Bean
    public AuthFilter getAuthFilter(final AuthenticationManager authenticationManager) {
        return new AuthFilter(PROTECTED_URLS, authenticationManager);
    }
}
