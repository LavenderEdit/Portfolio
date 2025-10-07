package com.portfolio.lavender.repository;

import com.portfolio.lavender.model.Skill;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Joan Lavender
 */
public interface SkillRepo extends JpaRepository<Skill, Long> {

    List<Skill> findAllByCategoryIdOrderBySortOrderAsc(Long categoryId);
}
