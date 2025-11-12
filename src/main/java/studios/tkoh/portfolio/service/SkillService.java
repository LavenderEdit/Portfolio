package studios.tkoh.portfolio.service;

import java.util.List;
import studios.tkoh.portfolio.dto.skill.SkillCategoryCreateRequest;
import studios.tkoh.portfolio.dto.skill.SkillCategoryDto;
import studios.tkoh.portfolio.dto.skill.SkillCategoryUpdateRequest;
import studios.tkoh.portfolio.dto.skill.SkillCreateRequest;
import studios.tkoh.portfolio.dto.skill.SkillDto;
import studios.tkoh.portfolio.dto.skill.SkillUpdateRequest;

/**
 *
 * @author Studios TKOH!
 */
public interface SkillService {

    // --- SkillCategory ---
    List<SkillCategoryDto> getAllCategories();

    SkillCategoryDto getCategoryById(Long categoryId);

    List<SkillCategoryDto> batchCreateCategories(List<SkillCategoryCreateRequest> createRequests);

    List<SkillCategoryDto> batchUpdateCategories(List<SkillCategoryUpdateRequest> updateRequests);

    void batchDeleteCategories(List<Long> categoryIds);

    // --- Skill (anidado) ---
    List<SkillDto> getSkillsForCategory(Long categoryId);

    List<SkillDto> batchCreateSkills(Long categoryId, List<SkillCreateRequest> createRequests);

    List<SkillDto> batchUpdateSkills(Long categoryId, List<SkillUpdateRequest> updateRequests);

    void batchDeleteSkills(Long categoryId, List<Long> skillIds);
}
