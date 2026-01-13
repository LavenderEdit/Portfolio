package studios.tkoh.portfolio.dto.publicapi;

import java.util.List;
import studios.tkoh.portfolio.dto.certificate.CertificateDto;
import studios.tkoh.portfolio.dto.education.EducationDto;
import studios.tkoh.portfolio.dto.experience.ExperienceDto;
import studios.tkoh.portfolio.dto.skill.SkillCategoryDto;
import studios.tkoh.portfolio.dto.social.SocialLinkDto;

/**
 *
 * @author Studios TKOH!
 */
public record PortfolioDetailDto(
        String slug,
        String fullName,
        String headline,
        String bio,
        String contactEmail,
        String location,
        String avatarUrl,
        String resumeUrl,
        boolean tkohCollab,
        List<SocialLinkDto> socialLinks,
        List<SkillCategoryDto> skillCategories,
        List<ProjectSummaryDto> projects,
        List<ExperienceDto> experiences,
        List<EducationDto> education,
        List<CertificateDto> certificates) {

}
