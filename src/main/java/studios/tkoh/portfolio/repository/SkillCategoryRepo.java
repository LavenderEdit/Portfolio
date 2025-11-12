package studios.tkoh.portfolio.repository;

import studios.tkoh.portfolio.model.SkillCategory;
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
public interface SkillCategoryRepo extends JpaRepository<SkillCategory, Long> {

    @EntityGraph(attributePaths = "skills")
    List<SkillCategory> findAllByProfileSlugOrderBySortOrderAsc(String slug);

    @EntityGraph(attributePaths = "skills")
    List<SkillCategory> findAllByProfileIdOrderBySortOrderAsc(Long profileId);

    @EntityGraph(attributePaths = "skills")
    Optional<SkillCategory> findByIdAndProfileId(Long id, Long profileId);

    List<SkillCategory> findAllByIdInAndProfileId(List<Long> ids, Long profileId);
}
