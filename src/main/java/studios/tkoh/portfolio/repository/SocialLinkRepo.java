package studios.tkoh.portfolio.repository;

import studios.tkoh.portfolio.model.SocialLink;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Studios TKOH!
 */
@Repository
public interface SocialLinkRepo extends JpaRepository<SocialLink, Long> {

    @EntityGraph(attributePaths = "profile")
    List<SocialLink> findAllByProfileSlugOrderBySortOrderAsc(String slug);

    List<SocialLink> findAllByProfileIdOrderBySortOrderAsc(Long profileId);

    Optional<SocialLink> findByIdAndProfileId(Long id, Long profileId);
}
