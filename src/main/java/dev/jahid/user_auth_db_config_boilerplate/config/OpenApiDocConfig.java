package dev.jahid.user_auth_db_config_boilerplate.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;

@OpenAPIDefinition(
        info = @Info(
                description = "Its the documentation for Spring boot boilerplate Application",
                contact = @Contact(
                        name = "MD Zanea Alam Jahid",
                        email = "jahidctgcse@gmail.com"
                ),
                version = "0.1.0"
        ),
        security = @SecurityRequirement( name = "bearerAuth" )
)
@SecuritySchemes({
        @SecurityScheme(
                name = "bearerAuth",
                description = "JWT Bearer Token Authentication",
                scheme = "bearer",
                type = SecuritySchemeType.HTTP,
                bearerFormat = "JWT",
                in = SecuritySchemeIn.HEADER
        )
})
public class OpenApiDocConfig {
}