package studios.tkoh.portfolio.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.portfolio.dto.skill.SkillCategoryCreateRequest;
import studios.tkoh.portfolio.dto.skill.SkillCategoryDto;
import studios.tkoh.portfolio.dto.skill.SkillCategoryUpdateRequest;
import studios.tkoh.portfolio.dto.skill.SkillCreateRequest;
import studios.tkoh.portfolio.dto.skill.SkillDto;
import studios.tkoh.portfolio.dto.skill.SkillUpdateRequest;
import studios.tkoh.portfolio.exception.ResourceNotFoundException;
import studios.tkoh.portfolio.mapper.SkillCategoryMapper;
import studios.tkoh.portfolio.mapper.SkillMapper;
import studios.tkoh.portfolio.model.Profile;
import studios.tkoh.portfolio.model.Skill;
import studios.tkoh.portfolio.model.SkillCategory;
import studios.tkoh.portfolio.repository.ProfileRepo;
import studios.tkoh.portfolio.repository.SkillCategoryRepo;
import studios.tkoh.portfolio.repository.SkillRepo;
import studios.tkoh.portfolio.security.CustomUserDetails;
import studios.tkoh.portfolio.service.SkillService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillCategoryRepo categoryRepository;
    private final SkillRepo skillRepository;
    private final ProfileRepo profileRepository;
    private final SkillCategoryMapper categoryMapper;
    private final SkillMapper skillMapper;

    // --- Implementación de SkillCategory ---
    @Override
    @Transactional(readOnly = true)
    public List<SkillCategoryDto> getAllCategories() {
        Long profileId = getAuthenticatedUser().getProfileId();
        return categoryRepository.findAllByProfileIdOrderBySortOrderAsc(profileId)
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SkillCategoryDto getCategoryById(Long categoryId) {
        Long profileId = getAuthenticatedUser().getProfileId();
        return categoryRepository.findByIdAndProfileId(categoryId, profileId)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("SkillCategory", "id", categoryId));
    }

    @Override
    @Transactional
    public List<SkillCategoryDto> batchCreateCategories(List<SkillCategoryCreateRequest> createRequests) {
        Long profileId = getAuthenticatedUser().getProfileId();
        Profile profile = profileRepository.getReferenceById(profileId);

        List<SkillCategory> newCategories = createRequests.stream()
                .map(req -> {
                    SkillCategory category = categoryMapper.toEntity(req);
                    category.setProfile(profile);
                    return category;
                })
                .toList();

        List<SkillCategory> savedCategories = categoryRepository.saveAll(newCategories);

        return savedCategories.stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public List<SkillCategoryDto> batchUpdateCategories(List<SkillCategoryUpdateRequest> updateRequests) {
        Long profileId = getAuthenticatedUser().getProfileId();
        List<Long> ids = updateRequests.stream().map(SkillCategoryUpdateRequest::id).toList();

        // Verificamos que todas las categorías pertenezcan al usuario
        Map<Long, SkillCategory> categoryMap = categoryRepository.findAllByIdInAndProfileId(ids, profileId)
                .stream()
                .collect(Collectors.toMap(SkillCategory::getId, Function.identity()));

        if (categoryMap.size() != ids.size()) {
            throw new ResourceNotFoundException("No se encontraron todas las SkillCategories o no pertenecen al usuario.");
        }

        List<SkillCategory> updatedCategories = updateRequests.stream()
                .map(req -> {
                    SkillCategory category = categoryMap.get(req.id());
                    categoryMapper.updateFromDto(req, category);
                    return category;
                })
                .toList();

        List<SkillCategory> savedCategories = categoryRepository.saveAll(updatedCategories);

        return savedCategories.stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void batchDeleteCategories(List<Long> categoryIds) {
        Long profileId = getAuthenticatedUser().getProfileId();

        // Obtenemos solo las que pertenecen al usuario
        List<SkillCategory> categoriesToDelete = categoryRepository.findAllByIdInAndProfileId(categoryIds, profileId);

        if (categoriesToDelete.isEmpty() && !categoryIds.isEmpty()) {
            throw new ResourceNotFoundException("Ninguna de las SkillCategories especificadas se encontró o pertenece al usuario.");
        }

        // Esto eliminará las categorías y, gracias a CascadeType.ALL y orphanRemoval=true
        // en la entidad SkillCategory, también eliminará todos los Skills anidados.
        categoryRepository.deleteAll(categoriesToDelete);
    }

    // --- Implementación de Skill (anidado) ---
    @Override
    @Transactional(readOnly = true)
    public List<SkillDto> getSkillsForCategory(Long categoryId) {
        Long profileId = getAuthenticatedUser().getProfileId();
        // Verificamos que la categoría padre exista y pertenezca al usuario
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("SkillCategory", "id", categoryId);
        }

        return skillRepository.findAllByCategoryIdAndCategory_Profile_Id(categoryId, profileId)
                .stream()
                .map(skillMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public List<SkillDto> batchCreateSkills(Long categoryId, List<SkillCreateRequest> createRequests) {
        Long profileId = getAuthenticatedUser().getProfileId();

        // Verificamos que la categoría padre pertenezca al usuario
        SkillCategory category = categoryRepository.findByIdAndProfileId(categoryId, profileId)
                .orElseThrow(() -> new ResourceNotFoundException("SkillCategory", "id", categoryId));

        List<Skill> newSkills = createRequests.stream()
                .map(req -> {
                    Skill skill = skillMapper.toEntity(req);
                    skill.setCategory(category); // Anidamos el skill a su categoría
                    return skill;
                })
                .toList();

        List<Skill> savedSkills = skillRepository.saveAll(newSkills);
        return savedSkills.stream()
                .map(skillMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public List<SkillDto> batchUpdateSkills(Long categoryId, List<SkillUpdateRequest> updateRequests) {
        Long profileId = getAuthenticatedUser().getProfileId();
        List<Long> ids = updateRequests.stream().map(SkillUpdateRequest::id).toList();

        // Verificamos que la categoría padre pertenezca al usuario
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("SkillCategory", "id", categoryId);
        }

        // Verificamos que todos los skills pertenezcan a esa categoría Y a ese perfil
        Map<Long, Skill> skillMap = skillRepository.findAllByIdInAndCategoryIdAndCategory_Profile_Id(ids, categoryId, profileId)
                .stream()
                .collect(Collectors.toMap(Skill::getId, Function.identity()));

        if (skillMap.size() != ids.size()) {
            throw new ResourceNotFoundException("No se encontraron todos los Skills o no pertenecen a la categoría/usuario.");
        }

        List<Skill> updatedSkills = updateRequests.stream()
                .map(req -> {
                    Skill skill = skillMap.get(req.id());
                    skillMapper.updateFromDto(req, skill);
                    return skill;
                })
                .toList();

        List<Skill> savedSkills = skillRepository.saveAll(updatedSkills);
        return savedSkills.stream()
                .map(skillMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void batchDeleteSkills(Long categoryId, List<Long> skillIds) {
        Long profileId = getAuthenticatedUser().getProfileId();

        // Verificamos que la categoría padre pertenezca al usuario
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("SkillCategory", "id", categoryId);
        }

        // Obtenemos solo los skills que pertenecen al usuario, a la categoría y están en la lista
        List<Skill> skillsToDelete = skillRepository.findAllByIdInAndCategoryIdAndCategory_Profile_Id(skillIds, categoryId, profileId);

        if (skillsToDelete.isEmpty() && !skillIds.isEmpty()) {
            throw new ResourceNotFoundException("Ninguno de los Skills especificados se encontró o pertenece a la categoría/usuario.");
        }

        skillRepository.deleteAll(skillsToDelete);
    }

    // --- Helper ---
    private CustomUserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new IllegalStateException("No se pudo obtener la información del usuario autenticado.");
        }
        return (CustomUserDetails) authentication.getPrincipal();
    }
}
