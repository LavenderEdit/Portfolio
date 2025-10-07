package com.portfolio.lavender.repository;

import com.portfolio.lavender.model.SkillCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Joan Lavender
 */
public interface SkillCategoryRepo extends JpaRepository<SkillCategory, Long> {

    List<SkillCategory> findAllByOrderBySortOrderAsc();
}
