package studios.tkoh.portfolio.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.portfolio.model.User;
import studios.tkoh.portfolio.repository.UserRepository;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmailWithProfile(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (user.getProfile() == null) {
            throw new IllegalStateException("User found but has no associated profile: " + user.getId());
        }

        Long profileId = user.getProfile().getId();

        return new CustomUserDetails(user, profileId);
    }
}
