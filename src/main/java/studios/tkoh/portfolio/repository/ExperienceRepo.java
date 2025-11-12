package studios.tkoh.portfolio.repository;

import studios.tkoh.portfolio.model.Experience;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Studios TKOH!
 */
public interface ExperienceRepo extends JpaRepository<Experience, Long> {

    List<Experience> findAllByProfileSlugOrderByStartDateDesc(String slug);

    List<Experience> findAllByProfileIdOrderByStartDateDesc(Long profileId);

    Optional<Experience> findByIdAndProfileId(Long id, Long profileId);
}
