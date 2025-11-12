package studios.tkoh.portfolio.repository;

import studios.tkoh.portfolio.model.Skill;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Studios TKOH!
 */
public interface SkillRepo extends JpaRepository<Skill, Long> {

    List<Skill> findAllByCategoryIdOrderBySortOrderAsc(Long categoryId);

    List<Skill> findAllByCategoryIdAndProfileId(Long categoryId, Long profileId);

    List<Skill> findAllByIdInAndCategoryIdAndProfileId(List<Long> ids, Long categoryId, Long profileId);
    
    List<Skill> findAllByIdInAndCategory_ProfileId(List<Long> ids, Long profileId);
}
