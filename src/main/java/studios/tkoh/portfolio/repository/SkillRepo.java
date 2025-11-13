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

    List<Skill> findAllByCategoryIdAndCategory_Profile_Id(Long categoryId, Long profileId);

    List<Skill> findAllByIdInAndCategoryIdAndCategory_Profile_Id(List<Long> ids, Long categoryId, Long profileId);

    List<Skill> findAllByIdInAndCategory_ProfileId(List<Long> ids, Long profileId);
}
