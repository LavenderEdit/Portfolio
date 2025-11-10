package studios.tkoh.portfolio.repository;

import studios.tkoh.portfolio.model.Project;
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
public interface ProjectRepo extends JpaRepository<Project, Long> {

    @EntityGraph(attributePaths = "skills")
    Optional<Project> findByProfileSlugAndSlug(String profileSlug, String slug);

    @EntityGraph(attributePaths = "skills")
    List<Project> findAllByProfileSlugOrderByFeaturedDescSortOrderAsc(String profileSlug);
}
