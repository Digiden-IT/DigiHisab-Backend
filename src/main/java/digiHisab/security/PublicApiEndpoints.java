package digiHisab.security;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PublicApiEndpoints {

    private final String[]  endpoints = {
            "/auth/**",
            "/api-docs/**",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };
}
