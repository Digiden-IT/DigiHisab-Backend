package digiHisab.security.service;

import digiHisab.security.CustomUserDetails;
import digiHisab.user.model.User;
import digiHisab.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService( UserRepository userRepository ) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername( String phoneNumber ) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNumber( phoneNumber )
                .orElseThrow( () -> new UsernameNotFoundException( "User not found with identity : " + phoneNumber ) );

        return new CustomUserDetails( user );
    }
}
