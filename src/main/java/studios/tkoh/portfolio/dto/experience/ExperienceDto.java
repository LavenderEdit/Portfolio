package studios.tkoh.portfolio.dto.experience;

import java.time.LocalDate;

/**
 *
 * @author Studios TKOH!
 */
public record ExperienceDto(
        Long id,
        String company,
        String role,
        String location,
        LocalDate startDate,
        LocalDate endDate,
        boolean current,
        String description) {

}
