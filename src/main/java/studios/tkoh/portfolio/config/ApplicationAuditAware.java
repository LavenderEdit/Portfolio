package studios.tkoh.portfolio.config;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import studios.tkoh.portfolio.security.CustomUserDetails;

/**
 *
 * @author Studios TKOH!
 */
public class ApplicationAuditAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.of("system");
        }

        String userEmail = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
        return Optional.of(userEmail);
    }
}
