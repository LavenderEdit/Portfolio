package studios.tkoh.portfolio.dto.auth;

import java.time.Instant;

public record VerificationStatusResponse(
        boolean emailVerified,
        Instant emailVerifiedAt) {
}
