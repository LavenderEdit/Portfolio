package studios.tkoh.portfolio.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.time.Clock;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.portfolio.dto.auth.AuthResponse;
import studios.tkoh.portfolio.dto.auth.LoginRequest;
import studios.tkoh.portfolio.dto.auth.RegisterRequest;
import studios.tkoh.portfolio.model.EmailVerificationToken;
import studios.tkoh.portfolio.model.Profile;
import studios.tkoh.portfolio.model.User;
import studios.tkoh.portfolio.repository.EmailVerificationTokenRepository;
import studios.tkoh.portfolio.repository.ProfileRepo;
import studios.tkoh.portfolio.repository.UserRepository;
import studios.tkoh.portfolio.security.AuthSessionService;
import studios.tkoh.portfolio.security.RefreshTokenService;
import studios.tkoh.portfolio.service.AuthService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ProfileRepo profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthSessionService authSessionService;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final Clock clock;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request, HttpServletRequest httpRequest) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalStateException("El correo ya esta en uso. Por favor intente otro.");
        }

        Profile profile = new Profile();
        profile.setFullName(request.fullName());
        profile.setContactEmail(request.email());
        profile.setBio("Bienvenido a mi portafolio!");
        profile.setHeadline("Desarrollador de Software");

        String baseSlug = request.fullName().toLowerCase()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9\\-]", "");
        String finalSlug = generateUniqueSlug(baseSlug);
        profile.setSlug(finalSlug);

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles("ROLE_USER")
                .emailVerified(false)
                .accountStatus("ACTIVE")
                .build();

        profile.setUser(user);
        user.setProfile(profile);

        User savedUser = userRepository.save(user);
        createEmailVerificationToken(savedUser);

        return authSessionService.createSession(savedUser.getEmail(), httpRequest);
    }

    @Override
    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        return authSessionService.createSession(request.email(), httpRequest);
    }

    @Override
    public AuthResponse refresh(String refreshToken, HttpServletRequest httpRequest) {
        return authSessionService.refresh(refreshToken, httpRequest);
    }

    @Override
    public void logout(String refreshToken) {
        authSessionService.logout(refreshToken);
    }

    @Override
    public void logoutAll(String email) {
        authSessionService.logoutAll(email);
    }

    private String generateUniqueSlug(String baseSlug) {
        String slug = baseSlug.isBlank() ? "portfolio" : baseSlug;
        int counter = 1;
        while (profileRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        return slug;
    }

    private void createEmailVerificationToken(User user) {
        byte[] bytes = new byte[48];
        secureRandom.nextBytes(bytes);
        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        EmailVerificationToken token = new EmailVerificationToken();
        token.setUser(user);
        token.setTokenHash(refreshTokenService.hash(rawToken));
        token.setCreatedAt(clock.instant());
        token.setExpiresAt(clock.instant().plusSeconds(24 * 60 * 60));
        token.setResendCount(0);
        emailVerificationTokenRepository.save(token);
    }
}
