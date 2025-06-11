package digiHisab.security;


import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

@Component
public class HashUtil {

    private final String ALGORITHM = "SHA-256";

    public String hash( String input ) {
        try {
            MessageDigest digest = MessageDigest.getInstance( this.ALGORITHM );
            byte[] hashedBytes = digest.digest( input.getBytes( StandardCharsets.UTF_8 ) );
            return Base64.getEncoder().encodeToString( hashedBytes );
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException( "SHA-256 algorithm not available", e );
        }
    }

    public boolean matches( String rawToken, String hashedToken ) {
        return Objects.equals( this.hash( rawToken ), hashedToken );
    }
}