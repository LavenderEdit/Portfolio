package studios.tkoh.portfolio.dto.education;

import java.time.LocalDate;

/**
 *
 * @author Studios TKOH!
 */
public record EducationDto(
        Long id,
        String institution,
        String degree,
        String field,
        LocalDate startDate,
        LocalDate endDate,
        String description) {

}
