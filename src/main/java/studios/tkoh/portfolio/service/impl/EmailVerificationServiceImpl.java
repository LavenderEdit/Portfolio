package studios.tkoh.portfolio.service.impl;

import java.security.SecureRandom;
import java.time.Clock;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import studios.tkoh.portfolio.dto.auth.VerificationStatusResponse;
import studios.tkoh.portfolio.model.EmailVerificationToken;
import studios.tkoh.portfolio.model.User;
import studios.tkoh.portfolio.repository.EmailVerificationTokenRepository;
import studios.tkoh.portfolio.repository.UserRepository;
import studios.tkoh.portfolio.security.RefreshTokenService;
import studios.tkoh.portfolio.service.EmailService;
import studios.tkoh.portfolio.service.EmailVerificationService;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final RefreshTokenService tokenHasher;
    private final EmailService emailService;
    private final Clock clock;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${application.security.email-verification.expiration-minutes:1440}")
    private long expirationMinutes;

    @Value("${application.security.email-verification.frontend-url:http://localhost:5173/verify-email}")
    private String frontendVerificationUrl;

    @Override
    @Transactional
    public void createAndSendVerification(User user) {
        if (user.isEmailVerified()) {
            return;
        }
        String rawToken = createToken(user, 0);
        emailService.sendEmailVerification(user, verificationUrl(rawToken));
    }

    @Override
    @Transactional
    public void verifyEmail(String rawToken) {
        EmailVerificationToken token = tokenRepository.findByTokenHash(tokenHasher.hash(rawToken))
                .orElseThrow(() -> new IllegalStateException("Token de verificacion invalido"));
        if (token.getConsumedAt() != null || !clock.instant().isBefore(token.getExpiresAt())) {
            throw new IllegalStateException("Token de verificacion invalido");
        }
        User user = token.getUser();
        user.setEmailVerified(true);
        user.setEmailVerifiedAt(clock.instant());
        token.setConsumedAt(clock.instant());
        userRepository.save(user);
        tokenRepository.save(token);
    }

    @Override
    @Transactional
    public void resendVerification(String email) {
        userRepository.findByEmail(email)
                .filter(user -> !user.isEmailVerified())
                .ifPresent(user -> {
                    String rawToken = createToken(user, nextResendCount(user));
                    emailService.sendEmailVerification(user, verificationUrl(rawToken));
                });
    }

    @Override
    @Transactional(readOnly = true)
    public VerificationStatusResponse status(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        return new VerificationStatusResponse(user.isEmailVerified(), user.getEmailVerifiedAt());
    }

    private String createToken(User user, int resendCount) {
        String rawToken = generateRawToken();
        EmailVerificationToken token = new EmailVerificationToken();
        token.setUser(user);
        token.setTokenHash(tokenHasher.hash(rawToken));
        token.setCreatedAt(clock.instant());
        token.setExpiresAt(clock.instant().plusSeconds(expirationMinutes * 60));
        token.setResendCount(resendCount);
        tokenRepository.save(token);
        return rawToken;
    }

    private int nextResendCount(User user) {
        return tokenRepository.findTopByUserEmailOrderByCreatedAtDesc(user.getEmail())
                .map(EmailVerificationToken::getResendCount)
                .map(count -> count + 1)
                .orElse(1);
    }

    private String generateRawToken() {
        byte[] bytes = new byte[48];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String verificationUrl(String rawToken) {
        return UriComponentsBuilder.fromUriString(frontendVerificationUrl)
                .queryParam("token", rawToken)
                .build()
                .toUriString();
    }
}
