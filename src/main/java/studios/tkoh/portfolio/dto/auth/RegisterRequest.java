package studios.tkoh.portfolio.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Studios TKOH!
 */
public record RegisterRequest(
        @NotBlank
        @Size(min = 3, max = 120)
        String fullName,
        @NotBlank
        @Email
        @Size(max = 150)
        String email,
        @NotBlank
        @Size(min = 8, max = 100)
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "La contrasena debe incluir mayuscula, minuscula y numero")
        String password) {

}
