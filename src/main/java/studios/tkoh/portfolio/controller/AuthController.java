package studios.tkoh.portfolio.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studios.tkoh.portfolio.dto.auth.AuthResponse;
import studios.tkoh.portfolio.dto.auth.LoginRequest;
import studios.tkoh.portfolio.dto.auth.RegisterRequest;
import studios.tkoh.portfolio.dto.auth.ResendVerificationRequest;
import studios.tkoh.portfolio.dto.auth.VerifyEmailRequest;
import studios.tkoh.portfolio.dto.response.ApiResponse;
import studios.tkoh.portfolio.security.AuthCookieService;
import studios.tkoh.portfolio.security.JwtService;
import studios.tkoh.portfolio.service.AuthService;
import studios.tkoh.portfolio.service.EmailVerificationService;

/**
 *
 * @author Studios TKOH!
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authenticationService;
    private final AuthCookieService authCookieService;
    private final JwtService jwtService;
    private final EmailVerificationService emailVerificationService;

    @Value("${application.security.jwt.refresh-expiration-days:7}")
    private long refreshExpirationDays;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        AuthResponse authResponse = authenticationService.register(request, httpRequest);
        addAuthCookies(httpResponse, authResponse);
        return ResponseEntity.ok(ApiResponse.ok("Usuario registrado exitosamente", authResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        AuthResponse authResponse = authenticationService.login(request, httpRequest);
        addAuthCookies(httpResponse, authResponse);
        return ResponseEntity.ok(ApiResponse.ok("Login exitoso", authResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @CookieValue(value = AuthCookieService.REFRESH_TOKEN_COOKIE, required = false) String refreshToken,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalStateException("Refresh token ausente");
        }
        AuthResponse authResponse = authenticationService.refresh(refreshToken, httpRequest);
        addAuthCookies(httpResponse, authResponse);
        return ResponseEntity.ok(ApiResponse.ok("Sesion renovada", authResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @CookieValue(value = AuthCookieService.REFRESH_TOKEN_COOKIE, required = false) String refreshToken,
            HttpServletResponse httpResponse
    ) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            authenticationService.logout(refreshToken);
        }
        clearAuthCookies(httpResponse);
        return ResponseEntity.ok(ApiResponse.ok("Sesion cerrada"));
    }

    @PostMapping("/logout-all")
    public ResponseEntity<ApiResponse<Void>> logoutAll(Authentication authentication, HttpServletResponse httpResponse) {
        if (authentication != null) {
            authenticationService.logoutAll(authentication.getName());
        }
        clearAuthCookies(httpResponse);
        return ResponseEntity.ok(ApiResponse.ok("Sesiones cerradas"));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        emailVerificationService.verifyEmail(request.token());
        return ResponseEntity.ok(ApiResponse.ok("Correo verificado exitosamente"));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<Void>> resendVerification(@Valid @RequestBody ResendVerificationRequest request) {
        emailVerificationService.resendVerification(request.email());
        return ResponseEntity.ok(ApiResponse.ok("Si la cuenta existe y requiere verificacion, enviaremos un correo"));
    }

    private void addAuthCookies(HttpServletResponse response, AuthResponse authResponse) {
        response.addCookie(authCookieService.createAccessTokenCookie(authResponse.token(), jwtService.getAccessTokenMaxAgeSeconds()));
        response.addCookie(authCookieService.createRefreshTokenCookie(authResponse.refreshToken(), refreshMaxAgeSeconds()));
    }

    private void clearAuthCookies(HttpServletResponse response) {
        response.addCookie(authCookieService.clearAccessTokenCookie());
        response.addCookie(authCookieService.clearRefreshTokenCookie());
    }

    private int refreshMaxAgeSeconds() {
        return Math.toIntExact(refreshExpirationDays * 24 * 60 * 60);
    }
}
