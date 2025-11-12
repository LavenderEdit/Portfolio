package studios.tkoh.portfolio.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.portfolio.dto.education.EducationDto;
import studios.tkoh.portfolio.dto.experience.ExperienceDto;
import studios.tkoh.portfolio.dto.project.ProjectDto;
import studios.tkoh.portfolio.dto.publicapi.PortfolioDetailDto;
import studios.tkoh.portfolio.dto.publicapi.PortfolioPublicDto;
import studios.tkoh.portfolio.dto.publicapi.ProjectSummaryDto;
import studios.tkoh.portfolio.dto.skill.SkillCategoryDto;
import studios.tkoh.portfolio.dto.social.SocialLinkDto;
import studios.tkoh.portfolio.exception.ResourceNotFoundException;
import studios.tkoh.portfolio.mapper.EducationMapper;
import studios.tkoh.portfolio.mapper.ExperienceMapper;
import studios.tkoh.portfolio.mapper.ProjectMapper;
import studios.tkoh.portfolio.mapper.PublicPortfolioMapper;
import studios.tkoh.portfolio.mapper.SkillCategoryMapper;
import studios.tkoh.portfolio.mapper.SocialLinkMapper;
import studios.tkoh.portfolio.model.Profile;
import studios.tkoh.portfolio.model.Project;
import studios.tkoh.portfolio.repository.EducationRepo;
import studios.tkoh.portfolio.repository.ExperienceRepo;
import studios.tkoh.portfolio.repository.ProfileRepo;
import studios.tkoh.portfolio.repository.ProjectRepo;
import studios.tkoh.portfolio.repository.SkillCategoryRepo;
import studios.tkoh.portfolio.repository.SocialLinkRepo;
import studios.tkoh.portfolio.service.PublicPortfolioService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicPortfolioServiceImpl implements PublicPortfolioService {

    private final ProfileRepo profileRepository;
    private final SocialLinkRepo socialLinkRepository;
    private final SkillCategoryRepo skillCategoryRepository;
    private final ProjectRepo projectRepository;
    private final ExperienceRepo experienceRepository;
    private final EducationRepo educationRepository;

    private final PublicPortfolioMapper publicMapper;
    private final SocialLinkMapper socialLinkMapper;
    private final SkillCategoryMapper skillCategoryMapper;
    private final ExperienceMapper experienceMapper;
    private final EducationMapper educationMapper;
    private final ProjectMapper projectMapper;

    @Override
    public List<PortfolioPublicDto> getAllPublicProfiles() {
        return profileRepository.findAll(Sort.by("fullName").ascending())
                .stream()
                .map(publicMapper::profileToPublicDto)
                .toList();
    }

    @Override
    public PortfolioDetailDto getFullPortfolioBySlug(String slug) {
        Profile profile = profileRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "slug", slug));

        PortfolioDetailDto dto = publicMapper.profileToDetailDto(profile);

        List<SocialLinkDto> socialLinks = socialLinkRepository.findAllByProfileSlugOrderBySortOrderAsc(slug)
                .stream().map(socialLinkMapper::toDto).toList();

        List<SkillCategoryDto> skillCategories = skillCategoryRepository.findAllByProfileSlugOrderBySortOrderAsc(slug)
                .stream().map(skillCategoryMapper::toDto).toList();

        List<ProjectSummaryDto> projects = projectRepository.findAllByProfileSlugOrderByFeaturedDescSortOrderAsc(slug)
                .stream().map(publicMapper::projectToSummaryDto).toList();

        List<ExperienceDto> experiences = experienceRepository.findAllByProfileSlugOrderByStartDateDesc(slug)
                .stream().map(experienceMapper::toDto).toList();

        List<EducationDto> education = educationRepository.findAllByProfileSlugOrderByStartDateDesc(slug)
                .stream().map(educationMapper::toDto).toList();

        return new PortfolioDetailDto(
                dto.slug(), dto.fullName(), dto.headline(), dto.bio(),
                dto.contactEmail(), dto.location(), dto.avatarUrl(), dto.resumeUrl(),
                socialLinks, skillCategories, projects, experiences, education
        );
    }

    @Override
    public ProjectDto getPublicProjectBySlugs(String profileSlug, String projectSlug) {
        Project project = projectRepository.findByProfileSlugAndSlug(profileSlug, projectSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "slug", projectSlug));

        return projectMapper.toDto(project);
    }
}
