package digiHisab.security;

import digiHisab.security.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor( onConstructor_ = {@Autowired} )
public class JwtUtil {

    private final CustomUserDetailsService userDetailsService;

    @Value("${jwt.access-token-secret}")
    private String accessTokenSecret;

    @Value("${jwt.refresh-token-secret}")
    private String refreshTokenSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshExpiration;

    private SecretKey getSignKeyForAccessToken() {
        return Keys.hmacShaKeyFor( Decoders.BASE64.decode( accessTokenSecret ) );
    }

    private SecretKey getSignKeyForRefreshToken() {
        return Keys.hmacShaKeyFor( Decoders.BASE64.decode( refreshTokenSecret ) );
    }

    public String generateToken( CustomUserDetails customUserDetails, Long expirationTimeInMilliseconds, TokenType tokenType ) {

        Date now = new Date();
        Date expiryDate = new Date( now.getTime() + expirationTimeInMilliseconds );

        Map<String, Object> claims = new HashMap<>();
        claims.put( "email", customUserDetails.getUser().getEmail() );
        claims.put( "tokenType", tokenType );

        if( tokenType.equals( TokenType.ACCESS ) )
            claims.put( "role", customUserDetails.getUser().getRole().getValue() );

        SecretKey secretKey = getSecretKey( tokenType );

        return Jwts.builder()
                .claims( claims )
                .issuedAt( now )
                .expiration( expiryDate )
                .compressWith( Jwts.ZIP.GZIP )
                .signWith( secretKey, Jwts.SIG.HS512 )
                .compact();
    }

    private SecretKey getSecretKey( TokenType tokenType ) {
        return tokenType.equals( TokenType.ACCESS ) ? getSignKeyForAccessToken() : getSignKeyForRefreshToken();
    }

    public String generateAccessToken( CustomUserDetails customUserDetails ) {
        return generateToken( customUserDetails, jwtExpiration, TokenType.ACCESS );
    }

    public String generateRefreshToken( CustomUserDetails customUserDetails ) {
        return generateToken( customUserDetails, refreshExpiration, TokenType.REFRESH );
    }

    public CustomUserDetails extractUser( String token, TokenType tokenType ) {
        SecretKey secretKey = getSecretKey( tokenType );

        Claims claims = Jwts.parser()
                .verifyWith( secretKey )
                .build()
                .parseSignedClaims( token )
                .getPayload();

        String email = (String) claims.get( "email" );
        return userDetailsService.loadUserByUsername( email ) ;
    }

    public boolean validateToken( String token, TokenType expectedTokenType ) {
        try {
            SecretKey secretKey = getSecretKey( expectedTokenType );
            Jwts.parser().verifyWith( secretKey ).build().parseSignedClaims( token );

            Claims claims = Jwts.parser()
                    .verifyWith( secretKey )
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String tokenTypeStr = (String) claims.get( "tokenType" );
            TokenType tokenType = TokenType.valueOf( tokenTypeStr );

            if ( !expectedTokenType.equals( tokenType ) ) {
                System.out.println( "Invalid token type: expected " + expectedTokenType + " but got " + tokenType );
                return false;
            }

            return true;
        } catch ( ExpiredJwtException e ) {
            System.out.println( "JWT expired at: " + e.getClaims().getExpiration() );
            return false;
        } catch ( JwtException | IllegalArgumentException e ) {
            System.out.println( "Invalid JWT: " + e.getMessage() );
            return false;
        }
    }
}