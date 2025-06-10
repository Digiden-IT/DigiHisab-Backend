package digiHisab.security.filter;

import digiHisab.security.JwtUtil;
import digiHisab.security.PublicApiEndpoints;
import digiHisab.security.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final PublicApiEndpoints publicApiEndpoints;

    public JwtAuthenticationFilter( JwtUtil jwtUtil, PublicApiEndpoints publicApiEndpoints ) {
        this.jwtUtil = jwtUtil;
        this.publicApiEndpoints = publicApiEndpoints;
    }

    @Override
    protected boolean shouldNotFilter( HttpServletRequest request ) {
        return Arrays.stream( publicApiEndpoints.getEndpoints() )
                .map( AntPathRequestMatcher::new )
                .anyMatch( matcher -> matcher.matches( request ) );
    }

    @Override
    protected void doFilterInternal( HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain chain ) throws ServletException, IOException {

        final String authHeader = request.getHeader( "Authorization" );

        if ( authHeader == null || !authHeader.startsWith( "Bearer " ) ) {
            chain.doFilter( request, response );
            return;
        }

        final String token = authHeader.substring( 7 );
        UserDetails userDetails = jwtUtil.extractUser( token, TokenType.ACCESS );

        if ( userDetails != null && SecurityContextHolder.getContext().getAuthentication() == null ) {

            if ( jwtUtil.validateToken( token, TokenType.ACCESS ) ) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken( userDetails, null, userDetails.getAuthorities() );

                authenticationToken.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );
                SecurityContextHolder.getContext().setAuthentication( authenticationToken );
            }
        }
        chain.doFilter( request, response );
    }
}

