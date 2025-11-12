package studios.tkoh.portfolio.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import studios.tkoh.portfolio.model.Certificate;

/**
 *
 * @author Studios TKOH!
 */
public interface CertificateRepo extends JpaRepository<Certificate, Long> {

    List<Certificate> findAllByProfileId(Long profileId);

    List<Certificate> findAllByProfileSlug(String profileSlug);

    Optional<Certificate> findByIdAndProfileId(Long id, Long profileId);
}
