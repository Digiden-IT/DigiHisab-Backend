package digiHisab.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

import digiHisab.user.model.User;

@Getter
public class CustomUserDetails implements UserDetails {

    User user;

    public CustomUserDetails( User user ) {
        this.user = user;
    }

    public Long getId(){
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of( new SimpleGrantedAuthority( this.user.getRole().getValue() ) );
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getName();
    }

    public String getPhoneNumber() {
        return this.user.getPhoneNumber();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.getIsActive();
    }
}

