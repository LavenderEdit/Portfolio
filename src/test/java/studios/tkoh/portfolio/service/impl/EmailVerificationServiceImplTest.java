package studios.tkoh.portfolio.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import studios.tkoh.portfolio.model.EmailVerificationToken;
import studios.tkoh.portfolio.model.User;
import studios.tkoh.portfolio.repository.EmailVerificationTokenRepository;
import studios.tkoh.portfolio.repository.UserRepository;
import studios.tkoh.portfolio.security.RefreshTokenService;
import studios.tkoh.portfolio.service.EmailService;

@ExtendWith(MockitoExtension.class)
class EmailVerificationServiceImplTest {

    @Mock
    private EmailVerificationTokenRepository tokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenService tokenHasher;

    @Mock
    private EmailService emailService;

    private final Clock clock = Clock.fixed(Instant.parse("2026-06-12T12:00:00Z"), ZoneOffset.UTC);

    @InjectMocks
    private EmailVerificationServiceImpl service;

    @Test
    void verifyEmailConsumesValidTokenAndMarksUserVerified() {
        ReflectionTestUtils.setField(service, "clock", clock);
        when(tokenHasher.hash("raw-token")).thenReturn("hashed-token");

        User user = User.builder()
                .email("user@example.com")
                .roles("ROLE_USER")
                .emailVerified(false)
                .accountStatus("ACTIVE")
                .build();
        EmailVerificationToken token = new EmailVerificationToken();
        token.setUser(user);
        token.setTokenHash("hashed-token");
        token.setCreatedAt(clock.instant().minusSeconds(60));
        token.setExpiresAt(clock.instant().plusSeconds(3600));

        when(tokenRepository.findByTokenHash("hashed-token")).thenReturn(Optional.of(token));

        service.verifyEmail("raw-token");

        assertThat(user.isEmailVerified()).isTrue();
        assertThat(user.getEmailVerifiedAt()).isEqualTo(clock.instant());
        assertThat(token.getConsumedAt()).isEqualTo(clock.instant());
        verify(userRepository).save(user);
        verify(tokenRepository).save(token);
    }

    @Test
    void verifyEmailRejectsConsumedToken() {
        ReflectionTestUtils.setField(service, "clock", clock);
        when(tokenHasher.hash("raw-token")).thenReturn("hashed-token");

        EmailVerificationToken token = new EmailVerificationToken();
        token.setConsumedAt(clock.instant().minusSeconds(1));
        token.setExpiresAt(clock.instant().plusSeconds(3600));

        when(tokenRepository.findByTokenHash("hashed-token")).thenReturn(Optional.of(token));

        assertThatThrownBy(() -> service.verifyEmail("raw-token"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Token de verificacion invalido");
    }

    @Test
    void resendVerificationSendsGenericEmailWhenUserExistsAndIsNotVerified() {
        ReflectionTestUtils.setField(service, "clock", clock);
        ReflectionTestUtils.setField(service, "expirationMinutes", 60L);
        ReflectionTestUtils.setField(service, "frontendVerificationUrl", "http://localhost:5173/verify-email");

        User user = User.builder()
                .email("user@example.com")
                .roles("ROLE_USER")
                .emailVerified(false)
                .accountStatus("ACTIVE")
                .build();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        service.resendVerification("user@example.com");

        verify(tokenRepository).save(any(EmailVerificationToken.class));
        verify(emailService).sendEmailVerification(any(User.class), any(String.class));
    }
}
