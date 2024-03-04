package org.maxym.spring.sensor.security.config;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.security.filter.JWTFilter;
import org.maxym.spring.sensor.security.service.AuthDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.maxym.spring.sensor.model.enums.Authorities.*;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthDetailsService authDetailsService;
    private final JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("/login", "/refresh", "/signup").permitAll()
                        .requestMatchers("/sensors").hasAnyAuthority(READ_SENSOR.name(), PERMIT_ALL.name())
                        .requestMatchers("/sensors/registration").hasAnyAuthority(CREATE_SENSOR.name(), PERMIT_ALL.name())
                        .requestMatchers("/measurements").hasAnyAuthority(READ_MEASUREMENT.name(), PERMIT_ALL.name())
                        .requestMatchers("/measurements/rainyDaysCount").hasAnyAuthority(READ_MEASUREMENT.name(), PERMIT_ALL.name())
                        .requestMatchers("/measurements/add").hasAnyAuthority(CREATE_MEASUREMENT.name(), PERMIT_ALL.name())
                        .requestMatchers("/users/**").hasAnyAuthority(READ_USERS.name(), PERMIT_ALL.name()))
                .sessionManagement(managementConfigurer -> managementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(authDetailsService)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}
