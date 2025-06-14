package digiHisab.auth.service;

import digiHisab.auth.request.LoginRequest;
import digiHisab.auth.request.RefreshRequest;
import digiHisab.auth.response.AuthResponse;
import digiHisab.security.CustomUserDetails;
import digiHisab.security.HashUtil;
import digiHisab.security.JwtUtil;
import digiHisab.security.TokenType;
import digiHisab.user.model.User;
import digiHisab.user.repository.UserRepository;
import digiHisab.user.service.LoggedInUserService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor( onConstructor_ = {@Autowired} )
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final HashUtil hashUtil;
    private final LoggedInUserService loggedInUserService;
    private final UserRepository userRepository;


    @Transactional
    public ResponseEntity<?> login( LoginRequest loginRequest ) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken( loginRequest.getPhoneNumber(), loginRequest.getPassword() )
        );
        SecurityContextHolder.getContext().setAuthentication( authentication );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String accessToken = jwtUtil.generateAccessToken( userDetails );
        String refreshToken = jwtUtil.generateRefreshToken( userDetails );
        String hashedRefreshToken = hashUtil.hash( refreshToken );

        userRepository.updateRefreshTokenById( userDetails.getUser().getId(), hashedRefreshToken );

        return ResponseEntity.ok( new AuthResponse( accessToken, refreshToken ) );
    }

    public AuthResponse refresh( RefreshRequest refreshRequest ) throws AuthenticationException {
        CustomUserDetails userDetails = jwtUtil.extractUser( refreshRequest.getToken(), TokenType.REFRESH );

        if( !hashUtil.matches( refreshRequest.getToken(), userDetails.getUser().getRefreshToken() ) )
            throw new AuthenticationException( "Invalid token" );

        String accessToken = jwtUtil.generateAccessToken( userDetails );
        return new AuthResponse( accessToken, refreshRequest.getToken() );
    }

    @Transactional
    public void logout() {
        User user = loggedInUserService.getLoginUser();
        userRepository.updateRefreshTokenById( user.getId(), null );
    }
}
