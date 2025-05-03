package dev.jahid.user_auth_db_config_boilerplate.auth.controller;

import dev.jahid.user_auth_db_config_boilerplate.auth.request.LoginRequest;
import dev.jahid.user_auth_db_config_boilerplate.auth.request.RefreshRequest;
import dev.jahid.user_auth_db_config_boilerplate.auth.service.AuthService;
import dev.jahid.user_auth_db_config_boilerplate.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "/auth" )
@RequiredArgsConstructor( onConstructor_ = {@Autowired} )
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping( "/login" )
    public ResponseEntity<?> login( @RequestBody LoginRequest loginRequest ) {
        return authService.login( loginRequest );
    }

    @PostMapping( "/refresh" )
    public ResponseEntity<?> refresh( @RequestBody RefreshRequest refreshRequest ) throws AuthenticationException {

        if ( !jwtUtil.validateToken( refreshRequest.getToken() ) ) {
            return ResponseEntity.status( 401 ).body( "Invalid or expired refresh token" );
        }
        return ResponseEntity.ok( authService.refresh( refreshRequest ) );
    }

    @GetMapping( "/logout" )
    public ResponseEntity<?> logout() {
        authService.logout();
        return ResponseEntity.ok( "Logged out successfully" );
    }
}
