package studios.tkoh.portfolio.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import studios.tkoh.portfolio.model.ContactMessage;
import studios.tkoh.portfolio.model.Profile;
import studios.tkoh.portfolio.service.EmailService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    @Override
    public void sendContactNotification(Profile recipientProfile, ContactMessage message) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(fromEmail);
            mail.setTo(recipientProfile.getContactEmail());
            mail.setSubject("Nuevo Mensaje de Contacto de: " + message.getName());

            String text = String.format("""
                                        Has recibido un nuevo mensaje a trav\u00e9s de tu portafolio '%s':
                                        
                                        De: %s
                                        Email: %s
                                        
                                        Mensaje:
                                        %s""",
                    recipientProfile.getFullName(),
                    message.getName(),
                    message.getEmail(),
                    message.getMessage()
            );
            mail.setText(text);

            mailSender.send(mail);
            log.info("Email de contacto enviado exitosamente a {}", recipientProfile.getContactEmail());

        } catch (MailException e) {
            log.error("Error al enviar email de contacto a {}: {}", recipientProfile.getContactEmail(), e.getMessage(), e);
        }
    }
}
