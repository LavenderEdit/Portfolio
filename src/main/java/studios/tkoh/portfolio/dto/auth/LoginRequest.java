package studios.tkoh.portfolio.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author Studios TKOH!
 */
public record LoginRequest(
        @NotBlank
        @Email
        String email,
        @NotBlank
        String password) {

}
