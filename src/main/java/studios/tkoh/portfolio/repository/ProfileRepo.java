package studios.tkoh.portfolio.repository;

import studios.tkoh.portfolio.model.Profile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Studios TKOH!
 */
@Repository
public interface ProfileRepo extends JpaRepository<Profile, Long> {
    
    Optional<Profile> findBySlug(String slug);

    boolean existsBySlug(String slug);
}
