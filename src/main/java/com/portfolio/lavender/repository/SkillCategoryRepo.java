package com.portfolio.lavender.repository;

import com.portfolio.lavender.model.SkillCategory;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Joan Lavender
 */
@Repository
public interface SkillCategoryRepo extends JpaRepository<SkillCategory, Long> {

    @EntityGraph(attributePaths = "skills")
    List<SkillCategory> findAllByProfileSlugOrderBySortOrderAsc(String slug);
}
