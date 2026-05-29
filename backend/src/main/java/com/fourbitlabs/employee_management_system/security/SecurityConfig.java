package com.fourbitlabs.employee_management_system.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] SWAGGER_PATHS = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**"
    };

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/auth/refresh", "/api/auth/logout",
                                "/api/admin/register", "/api", "/")
                        .permitAll()
                        .requestMatchers(SWAGGER_PATHS).permitAll()

                        // Overlapping GET mappings for Dropdowns and Mappings in Dashboards
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/admin/trainers/**")
                        .hasAnyRole("ADMIN", "ANALYST", "COUNSELLOR", "TRAINER")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/analyst/batches/**")
                        .hasAnyRole("ADMIN", "ANALYST", "COUNSELLOR", "TRAINER")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/counsellor/students/**")
                        .hasAnyRole("ADMIN", "COUNSELLOR", "ANALYST", "TRAINER")

                        // Strict Write/Manage Mappings
                        .requestMatchers("/api/admin", "/api/admin/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/counsellor", "/api/counsellor/**").hasAnyRole("COUNSELLOR", "ADMIN")
                        .requestMatchers("/api/trainer", "/api/trainer/**").hasAnyRole("TRAINER", "ADMIN")
                        .requestMatchers("/api/analyst", "/api/analyst/**").hasAnyRole("ANALYST", "ADMIN")
                        .requestMatchers("/api/assignments", "/api/assignments/**")
                        .hasAnyRole("ADMIN", "ANALYST", "COUNSELLOR", "TRAINER")
                        .requestMatchers("/api/batch-progress", "/api/batch-progress/**")
                        .hasAnyRole("ADMIN", "TRAINER", "ANALYST", "COUNSELLOR")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
