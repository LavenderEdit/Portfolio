package studios.tkoh.portfolio.config;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

        Object principal = authentication.getPrincipal();
        String userEmail = "system";

        if (principal instanceof CustomUserDetails) {
            userEmail = ((CustomUserDetails) principal).getUsername();
        } else if (principal instanceof OAuth2User) {
            userEmail = ((OAuth2User) principal).getAttribute("email");
        } else {
            userEmail = authentication.getName();
        }

        return Optional.ofNullable(userEmail);
    }
}

