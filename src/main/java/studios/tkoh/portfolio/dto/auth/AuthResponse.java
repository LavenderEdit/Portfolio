package studios.tkoh.portfolio.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Studios TKOH!
 */
public record AuthResponse(
        String token,
        @JsonIgnore String refreshToken,
        boolean emailVerified) {

    public AuthResponse(String token) {
        this(token, null, false);
    }

}
