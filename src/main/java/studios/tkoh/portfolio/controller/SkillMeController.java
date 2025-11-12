package studios.tkoh.portfolio.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studios.tkoh.portfolio.dto.response.ApiResponse;
import studios.tkoh.portfolio.dto.skill.BatchDeleteRequest;
import studios.tkoh.portfolio.dto.skill.SkillCategoryCreateRequest;
import studios.tkoh.portfolio.dto.skill.SkillCategoryDto;
import studios.tkoh.portfolio.dto.skill.SkillCategoryUpdateRequest;
import studios.tkoh.portfolio.dto.skill.SkillCreateRequest;
import studios.tkoh.portfolio.dto.skill.SkillDto;
import studios.tkoh.portfolio.dto.skill.SkillUpdateRequest;
import studios.tkoh.portfolio.service.SkillService;

/**
 *
 * @author Studios TKOH!
 */
@RestController
@RequestMapping("/api/me/skill-categories")
@RequiredArgsConstructor
public class SkillMeController {

    private final SkillService skillService;

    // --- SkillCategory Endpoints ---
    @GetMapping
    public ResponseEntity<ApiResponse<List<SkillCategoryDto>>> getAllCategories() {
        List<SkillCategoryDto> categories = skillService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.ok("Categorías de skills obtenidas", categories));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<SkillCategoryDto>> getCategoryById(@PathVariable Long categoryId) {
        SkillCategoryDto category = skillService.getCategoryById(categoryId);
        return ResponseEntity.ok(ApiResponse.ok("Categoría de skill obtenida", category));
    }

    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<List<SkillCategoryDto>>> batchCreateCategories(
            @Valid @RequestBody List<SkillCategoryCreateRequest> createRequests) {
        List<SkillCategoryDto> newCategories = skillService.batchCreateCategories(createRequests);
        return new ResponseEntity<>(ApiResponse.ok("Categorías creadas exitosamente", newCategories), HttpStatus.CREATED);
    }

    @PutMapping("/batch")
    public ResponseEntity<ApiResponse<List<SkillCategoryDto>>> batchUpdateCategories(
            @Valid @RequestBody List<SkillCategoryUpdateRequest> updateRequests) {
        List<SkillCategoryDto> updatedCategories = skillService.batchUpdateCategories(updateRequests);
        return ResponseEntity.ok(ApiResponse.ok("Categorías actualizadas exitosamente", updatedCategories));
    }

    @DeleteMapping("/batch")
    public ResponseEntity<ApiResponse<Void>> batchDeleteCategories(
            @Valid @RequestBody BatchDeleteRequest deleteRequest) {
        skillService.batchDeleteCategories(deleteRequest.ids());
        return ResponseEntity.ok(ApiResponse.ok("Categorías eliminadas exitosamente"));
    }

    // --- Skill (anidado) Endpoints ---
    @GetMapping("/{categoryId}/skills")
    public ResponseEntity<ApiResponse<List<SkillDto>>> getSkillsForCategory(@PathVariable Long categoryId) {
        List<SkillDto> skills = skillService.getSkillsForCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.ok("Skills obtenidos para la categoría", skills));
    }

    @PostMapping("/{categoryId}/skills/batch")
    public ResponseEntity<ApiResponse<List<SkillDto>>> batchCreateSkills(
            @PathVariable Long categoryId,
            @Valid @RequestBody List<SkillCreateRequest> createRequests) {
        List<SkillDto> newSkills = skillService.batchCreateSkills(categoryId, createRequests);
        return new ResponseEntity<>(ApiResponse.ok("Skills creados exitosamente", newSkills), HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}/skills/batch")
    public ResponseEntity<ApiResponse<List<SkillDto>>> batchUpdateSkills(
            @PathVariable Long categoryId,
            @Valid @RequestBody List<SkillUpdateRequest> updateRequests) {
        List<SkillDto> updatedSkills = skillService.batchUpdateSkills(categoryId, updateRequests);
        return ResponseEntity.ok(ApiResponse.ok("Skills actualizados exitosamente", updatedSkills));
    }

    @DeleteMapping("/{categoryId}/skills/batch")
    public ResponseEntity<ApiResponse<Void>> batchDeleteSkills(
            @PathVariable Long categoryId,
            @Valid @RequestBody BatchDeleteRequest deleteRequest) {
        skillService.batchDeleteSkills(categoryId, deleteRequest.ids());
        return ResponseEntity.ok(ApiResponse.ok("Skills eliminados exitosamente"));
    }
}
