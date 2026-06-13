package studios.tkoh.portfolio.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.portfolio.dto.auth.AuthResponse;
import studios.tkoh.portfolio.model.User;
import studios.tkoh.portfolio.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthSessionService {

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public AuthResponse createSession(String email, HttpServletRequest request) {
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);
        String accessToken = jwtService.generateToken(userDetails);
        User user = userRepository.findByEmailWithProfile(email)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        RefreshTokenService.IssuedRefreshToken refreshToken = refreshTokenService.create(user, request);
        return new AuthResponse(accessToken, refreshToken.rawToken(), user.isEmailVerified());
    }

    @Transactional
    public AuthResponse refresh(String rawRefreshToken, HttpServletRequest request) {
        RefreshTokenService.IssuedRefreshToken rotated = refreshTokenService.rotate(rawRefreshToken, request);
        User user = rotated.entity().getUser();
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(user.getEmail());
        String accessToken = jwtService.generateToken(userDetails);
        return new AuthResponse(accessToken, rotated.rawToken(), user.isEmailVerified());
    }

    @Transactional
    public void logout(String rawRefreshToken) {
        refreshTokenService.revoke(rawRefreshToken);
    }

    @Transactional
    public void logoutAll(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        refreshTokenService.revokeAll(user);
    }
}
