package studios.tkoh.portfolio.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 *
 * @author Studios TKOH!
 */
public record ProjectUpdateRequest(
        @NotNull(message = "El ID es obligatorio para actualizar")
        Long id,
        @NotBlank(message = "El título no puede estar vacío")
        @Size(max = 140)
        String title,
        @NotBlank(message = "El resumen no puede estar vacío")
        @Size(max = 280)
        String summary,
        @Size(max = 65535)
        String description,
        @Size(max = 512)
        String repoUrl,
        @Size(max = 512)
        String liveUrl,
        @Size(max = 512)
        String coverImage,
        LocalDate startDate,
        LocalDate endDate,
        @NotNull
        boolean featured,
        @NotNull
        int sortOrder) {

}
