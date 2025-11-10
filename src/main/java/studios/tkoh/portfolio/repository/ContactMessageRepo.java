package studios.tkoh.portfolio.repository;

import studios.tkoh.portfolio.model.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Studios TKOH!
 */
public interface ContactMessageRepo extends JpaRepository<ContactMessage, Long> {
}
