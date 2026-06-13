package studios.tkoh.portfolio.service;

import studios.tkoh.portfolio.model.ContactMessage;
import studios.tkoh.portfolio.model.Profile;
import studios.tkoh.portfolio.model.User;

/**
 *
 * @author Studios TKOH!
 */
public interface EmailService {

    void sendContactNotification(Profile recipientProfile, ContactMessage message);

    void sendEmailVerification(User user, String verificationUrl);
}
