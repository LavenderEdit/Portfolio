package studios.tkoh.portfolio.security;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.portfolio.model.RefreshToken;
import studios.tkoh.portfolio.model.User;
import studios.tkoh.portfolio.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final Clock clock;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${application.security.jwt.refresh-expiration-days:7}")
    private long refreshExpirationDays;

    @Transactional
    public IssuedRefreshToken create(User user, HttpServletRequest request) {
        String rawToken = generateRawToken();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setTokenHash(hash(rawToken));
        refreshToken.setIssuedAt(clock.instant());
        refreshToken.setExpiresAt(clock.instant().plusSeconds(refreshExpirationDays * 24 * 60 * 60));
        refreshToken.setIpAddress(clientIp(request));
        refreshToken.setUserAgent(userAgent(request));
        RefreshToken saved = refreshTokenRepository.save(refreshToken);
        return new IssuedRefreshToken(rawToken, saved);
    }

    @Transactional
    public IssuedRefreshToken rotate(String rawToken, HttpServletRequest request) {
        RefreshToken existing = refreshTokenRepository.findByTokenHash(hash(rawToken))
                .orElseThrow(() -> new IllegalStateException("Refresh token invalido"));
        Instant now = clock.instant();
        if (existing.getRevokedAt() != null) {
            refreshTokenRepository.revokeAllActiveByUserId(existing.getUser().getId(), now);
            throw new IllegalStateException("Refresh token reutilizado");
        }
        if (!now.isBefore(existing.getExpiresAt())) {
            existing.setRevokedAt(now);
            throw new IllegalStateException("Refresh token expirado");
        }

        IssuedRefreshToken replacement = create(existing.getUser(), request);
        existing.setRevokedAt(now);
        existing.setReplacedByToken(replacement.entity());
        return replacement;
    }

    @Transactional
    public void revoke(String rawToken) {
        refreshTokenRepository.findByTokenHash(hash(rawToken)).ifPresent(token -> token.setRevokedAt(clock.instant()));
    }

    @Transactional
    public void revokeAll(User user) {
        refreshTokenRepository.revokeAllActiveByUserId(user.getId(), clock.instant());
    }

    public String hash(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 no disponible", ex);
        }
    }

    private String generateRawToken() {
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String clientIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String userAgent(HttpServletRequest request) {
        return request == null ? null : request.getHeader("User-Agent");
    }

    public record IssuedRefreshToken(String rawToken, RefreshToken entity) {
    }
}
