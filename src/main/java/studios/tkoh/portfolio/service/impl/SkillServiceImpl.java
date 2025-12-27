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
import studios.tkoh.portfolio.dto.skill.GlobalSkillDto;
import studios.tkoh.portfolio.dto.skill.SkillCategoryCreateRequest;
import studios.tkoh.portfolio.dto.skill.SkillCategoryDto;
import studios.tkoh.portfolio.dto.skill.SkillCategoryUpdateRequest;
import studios.tkoh.portfolio.dto.skill.SkillCreateRequest;
import studios.tkoh.portfolio.dto.skill.SkillDto;
import studios.tkoh.portfolio.dto.skill.SkillUpdateRequest;
import studios.tkoh.portfolio.exception.ResourceNotFoundException;
import studios.tkoh.portfolio.mapper.SkillCategoryMapper;
import studios.tkoh.portfolio.mapper.SkillMapper;
import studios.tkoh.portfolio.model.GlobalSkill;
import studios.tkoh.portfolio.model.Profile;
import studios.tkoh.portfolio.model.Skill;
import studios.tkoh.portfolio.model.SkillCategory;
import studios.tkoh.portfolio.repository.GlobalSkillRepo;
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
    private final GlobalSkillRepo globalSkillRepository;
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

        List<SkillCategory> categoriesToDelete = categoryRepository.findAllByIdInAndProfileId(categoryIds, profileId);

        if (categoriesToDelete.isEmpty() && !categoryIds.isEmpty()) {
            throw new ResourceNotFoundException("Ninguna de las SkillCategories especificadas se encontró o pertenece al usuario.");
        }

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

        SkillCategory category = categoryRepository.findByIdAndProfileId(categoryId, profileId)
                .orElseThrow(() -> new ResourceNotFoundException("SkillCategory", "id", categoryId));

        List<Skill> newSkills = createRequests.stream()
                .map(req -> {
                    GlobalSkill globalSkill = globalSkillRepository.findByNameIgnoreCase(req.name())
                            .orElseGet(() -> {
                                GlobalSkill newGlobal = new GlobalSkill();
                                newGlobal.setName(req.name());
                                newGlobal.setIconUrl(req.icon());
                                return globalSkillRepository.save(newGlobal);
                            });

                    Skill skill = new Skill();
                    skill.setCategory(category);
                    skill.setGlobalSkill(globalSkill);
                    skill.setLevel(req.level());
                    skill.setSortOrder(req.sortOrder());

                    if (req.icon() != null && !req.icon().equals(globalSkill.getIconUrl())) {
                        skill.setIcon(req.icon());
                    }

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

        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("SkillCategory", "id", categoryId);
        }

        Map<Long, Skill> skillMap = skillRepository.findAllByIdInAndCategoryIdAndCategory_Profile_Id(ids, categoryId, profileId)
                .stream()
                .collect(Collectors.toMap(Skill::getId, Function.identity()));

        if (skillMap.size() != ids.size()) {
            throw new ResourceNotFoundException("Error al encontrar skills para actualizar");
        }

        List<Skill> updatedSkills = updateRequests.stream()
                .map(req -> {
                    Skill skill = skillMap.get(req.id());
                    skill.setLevel(req.level());
                    skill.setSortOrder(req.sortOrder());
                    if (req.icon() != null) {
                        skill.setIcon(req.icon());
                    }
                    return skill;
                })
                .toList();

        return skillRepository.saveAll(updatedSkills).stream().map(skillMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void batchDeleteSkills(Long categoryId, List<Long> skillIds) {
        Long profileId = getAuthenticatedUser().getProfileId();

        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("SkillCategory", "id", categoryId);
        }

        List<Skill> skillsToDelete = skillRepository.findAllByIdInAndCategoryIdAndCategory_Profile_Id(skillIds, categoryId, profileId);

        if (skillsToDelete.isEmpty() && !skillIds.isEmpty()) {
            throw new ResourceNotFoundException("Ninguno de los Skills especificados se encontró o pertenece a la categoría/usuario.");
        }

        skillRepository.deleteAll(skillsToDelete);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GlobalSkillDto> searchGlobalSkills(String query) {
        return globalSkillRepository.findByNameContainingIgnoreCaseOrderByNameAsc(query)
                .stream()
                .map(gs -> new GlobalSkillDto(gs.getId(), gs.getName(), gs.getIconUrl()))
                .limit(20)
                .toList();
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
