package com.raj.AnimalMovements.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.raj.AnimalMovements.security.JwtRequestFilter;

@Configuration
@EnableMethodSecurity // Replaces the deprecated @EnableGlobalMethodSecurity
public class SecurityConfig {

    // private final AuthenticatonProvider authenticationProvider;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    // @Bean
    // public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenUtil
    // jwtTokenUtil, UserService userService) {
    // return new JwtAuthenticationFilter(jwtTokenUtil, userService);
    // }

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    // return new BCryptPasswordEncoder();
    // }

    // @Bean
    // public AuthenticationManager
    // authenticationManager(AuthenticationConfiguration authConfig) throws
    // Exception {
    // return authConfig.getAuthenticationManager();
    // }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api/auth/**",
                                "/api/public/**")
                        .permitAll()
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfig = new CorsConfiguration();
                    corsConfig.addAllowedOriginPattern("*"); // Updated to allow all origins
                    corsConfig.addAllowedMethod("*");
                    corsConfig.addAllowedHeader("*");
                    corsConfig.setAllowCredentials(true); // Add this line for security
                    return corsConfig;
                }));

        return http.build();
    }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
    // Exception {
    // http.csrf().disable()
    // .authorizeHttpRequests()
    // .anyRequest().permitAll(); // Allow all requests without authentication
    // return http.build();
    // }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
    // Exception {
    // http.csrf().disable()
    // .authorizeHttpRequests()
    // .requestMatchers("/api/auth/**").permitAll() // Public endpoints
    // .requestMatchers("/swagger-ui/**").permitAll() // Public endpoints
    // .requestMatchers("/v3/api-docs/**").permitAll() // Public endpoints
    // .anyRequest().authenticated() // All other endpoints require authentication
    // .and()
    // .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    // .and()
    // .addFilterBefore(new JwtRequestFilter(),
    // UsernamePasswordAuthenticationFilter.class);

    // return http.build();
    // }
    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http,
    // JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
    // http
    // .csrf(csrf -> csrf.disable())
    // .sessionManagement(session ->
    // session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    // .authorizeHttpRequests(auth -> auth
    // // Public endpoints
    // .requestMatchers("/api/public/**").permitAll()
    // .requestMatchers("/api/auth/test").permitAll()
    // .requestMatchers("/swagger-ui/**").permitAll()

    // // Endpoints requiring authentication but no specific role
    // .requestMatchers("/api/user/**").authenticated()

    // // Role-based endpoints
    // .requestMatchers("/api/admin/**").hasRole("ADMIN")

    // // All other requests require authentication
    // .anyRequest().authenticated()
    // )
    // // Add the JWT authentication filter before Spring Security's
    // UsernamePasswordAuthenticationFilter.
    // .addFilterBefore(jwtAuthenticationFilter,
    // UsernamePasswordAuthenticationFilter.class)
    // .cors(cors -> cors.configurationSource(request -> {
    // CorsConfiguration corsConfig = new CorsConfiguration();
    // corsConfig.addAllowedOrigin("*");
    // corsConfig.addAllowedMethod("*");
    // corsConfig.addAllowedHeader("*");
    // return corsConfig;
    // }));

    // return http.build();
    // }
}
