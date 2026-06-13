package studios.tkoh.portfolio.security;

import java.time.Clock;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.portfolio.model.Profile;
import studios.tkoh.portfolio.model.User;
import studios.tkoh.portfolio.model.UserIdentity;
import studios.tkoh.portfolio.repository.ProfileRepo;
import studios.tkoh.portfolio.repository.UserIdentityRepository;
import studios.tkoh.portfolio.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class OAuth2UserProvisioningService {

    private static final String GOOGLE = "google";

    private final UserIdentityRepository userIdentityRepository;
    private final UserRepository userRepository;
    private final ProfileRepo profileRepository;
    private final Clock clock;

    @Transactional
    public User provisionGoogleUser(String subject, String email, boolean emailVerified, String name, String picture) {
        return userIdentityRepository.findByProviderAndProviderSubject(GOOGLE, subject)
                .map(UserIdentity::getUser)
                .orElseGet(() -> linkOrCreateGoogleUser(subject, email, emailVerified, name, picture));
    }

    private User linkOrCreateGoogleUser(String subject, String email, boolean emailVerified, String name, String picture) {
        User user = null;
        if (emailVerified) {
            user = userRepository.findByEmail(email).orElse(null);
        }
        if (user == null) {
            user = createGoogleOnlyUser(email, emailVerified, name, picture);
        } else if (emailVerified && !user.isEmailVerified()) {
            user.setEmailVerified(true);
            user.setEmailVerifiedAt(clock.instant());
        }

        UserIdentity identity = new UserIdentity();
        identity.setUser(user);
        identity.setProvider(GOOGLE);
        identity.setProviderSubject(subject);
        identity.setProviderEmail(email);
        identity.setProviderEmailVerified(emailVerified);
        identity.setProviderAvatarUrl(picture);
        identity.setCreatedAt(clock.instant());
        identity.setUpdatedAt(clock.instant());
        userIdentityRepository.save(identity);
        return user;
    }

    private User createGoogleOnlyUser(String email, boolean emailVerified, String name, String picture) {
        String displayName = name == null || name.isBlank() ? email.substring(0, email.indexOf('@')) : name;

        Profile profile = new Profile();
        profile.setFullName(displayName);
        profile.setContactEmail(email);
        profile.setBio("Bienvenido a mi portafolio!");
        profile.setHeadline("Desarrollador de Software");
        profile.setAvatarUrl(picture);
        profile.setSlug(generateUniqueSlug(displayName));

        User user = User.builder()
                .email(email)
                .password(null)
                .roles("ROLE_USER")
                .emailVerified(emailVerified)
                .emailVerifiedAt(emailVerified ? Instant.now(clock) : null)
                .accountStatus("ACTIVE")
                .build();
        profile.setUser(user);
        user.setProfile(profile);
        return userRepository.save(user);
    }

    private String generateUniqueSlug(String name) {
        String baseSlug = name.toLowerCase()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9\\-]", "");
        if (baseSlug.isBlank()) {
            baseSlug = "portfolio";
        }
        String slug = baseSlug;
        int counter = 1;
        while (profileRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        return slug;
    }
}
