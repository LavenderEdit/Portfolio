package studios.tkoh.portfolio.service;

import jakarta.servlet.http.HttpServletRequest;
import studios.tkoh.portfolio.dto.auth.AuthResponse;
import studios.tkoh.portfolio.dto.auth.LoginRequest;
import studios.tkoh.portfolio.dto.auth.RegisterRequest;

/**
 *
 * @author Studios TKOH!
 */
public interface AuthService {

    AuthResponse register(RegisterRequest request, HttpServletRequest httpRequest);

    AuthResponse login(LoginRequest request, HttpServletRequest httpRequest);

    AuthResponse refresh(String refreshToken, HttpServletRequest httpRequest);

    void logout(String refreshToken);

    void logoutAll(String email);
}
