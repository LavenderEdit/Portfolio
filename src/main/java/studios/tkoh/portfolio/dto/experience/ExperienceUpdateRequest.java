package studios.tkoh.portfolio.dto.experience;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 *
 * @author Studios TKOH!
 */
public record ExperienceUpdateRequest(
        @NotNull(message = "El ID es obligatorio para actualizar")
        Long id,
        @NotBlank(message = "El nombre de la compañía no puede estar vacío")
        @Size(max = 150)
        String company,
        @NotBlank(message = "El cargo no puede estar vacío")
        @Size(max = 150)
        String role,
        @Size(max = 100)
        String location,
        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate startDate,
        LocalDate endDate,
        @NotNull
        boolean current,
        @Size(max = 65535)
        String description) {

}
