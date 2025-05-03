package dev.jahid.user_auth_db_config_boilerplate.security;

import dev.jahid.user_auth_db_config_boilerplate.security.filter.JwtAuthenticationFilter;
import dev.jahid.user_auth_db_config_boilerplate.security.service.CustomUserDetailsService;
import dev.jahid.user_auth_db_config_boilerplate.user.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final PublicApiEndpoints publicApiEndpoints;

    public SecurityConfig(
            CustomUserDetailsService customUserDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            PublicApiEndpoints publicApiEndpoints
    ) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.publicApiEndpoints = publicApiEndpoints;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService( customUserDetailsService );
        provider.setPasswordEncoder( passwordEncoder() );
        return new ProviderManager( List.of( provider ) );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder( 16, 32, 1, 65536, 10 );
    }


    @Bean
    public SecurityFilterChain securityFilterChain( HttpSecurity http ) throws Exception {
        return http
                .cors( Customizer.withDefaults() )
                .sessionManagement( AbstractHttpConfigurer::disable )
                .csrf( AbstractHttpConfigurer::disable )
                .httpBasic( AbstractHttpConfigurer::disable )
                .formLogin( AbstractHttpConfigurer::disable )
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers( publicApiEndpoints.getEndpoints() ).permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore( jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class )
                .build();
    }
}
