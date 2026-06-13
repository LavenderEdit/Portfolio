package studios.tkoh.portfolio.service;

import studios.tkoh.portfolio.dto.auth.VerificationStatusResponse;
import studios.tkoh.portfolio.model.User;

public interface EmailVerificationService {

    void createAndSendVerification(User user);

    void verifyEmail(String rawToken);

    void resendVerification(String email);

    VerificationStatusResponse status(String email);
}
