package studios.tkoh.portfolio.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import studios.tkoh.portfolio.model.User;

/**
 *
 * @author Studios TKOH!
 */
public class CustomUserDetails implements UserDetails {

    private final User user;
    private final Long profileId;

    public CustomUserDetails(User user, Long profileId) {
        this.user = user;
        this.profileId = profileId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public Long getUserId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
