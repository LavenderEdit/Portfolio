package studios.tkoh.portfolio.repository;

import studios.tkoh.portfolio.model.Experience;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Studios TKOH!
 */
public interface ExperienceRepo extends JpaRepository<Experience, Long> {

    List<Experience> findAllByProfileSlugOrderByStartDateDesc(String slug);
}
