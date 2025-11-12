package studios.tkoh.portfolio.repository;

import studios.tkoh.portfolio.model.Education;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Studios TKOH!
 */
public interface EducationRepo extends JpaRepository<Education, Long> {

    List<Education> findAllByProfileSlugOrderByStartDateDesc(String slug);

    List<Education> findAllByProfileIdOrderByStartDateDesc(Long profileId);

    Optional<Education> findByIdAndProfileId(Long id, Long profileId);
}
