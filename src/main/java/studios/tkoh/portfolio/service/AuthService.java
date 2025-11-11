package studios.tkoh.portfolio.service;

import studios.tkoh.portfolio.dto.auth.AuthResponse;
import studios.tkoh.portfolio.dto.auth.LoginRequest;
import studios.tkoh.portfolio.dto.auth.RegisterRequest;

/**
 *
 * @author Studios TKOH!
 */
public interface AuthService {
    
    AuthResponse register(RegisterRequest request);
    
    AuthResponse login(LoginRequest request);
}
