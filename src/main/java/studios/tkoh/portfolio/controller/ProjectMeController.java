package studios.tkoh.portfolio.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studios.tkoh.portfolio.controller.generic.DtoCrudController;
import studios.tkoh.portfolio.dto.project.ProjectCreateRequest;
import studios.tkoh.portfolio.dto.project.ProjectDto;
import studios.tkoh.portfolio.dto.project.ProjectUpdateRequest;
import studios.tkoh.portfolio.dto.response.ApiResponse;
import studios.tkoh.portfolio.service.ProjectService;

/**
 *
 * @author Studios TKOH!
 */
@RestController
@RequestMapping("/api/me/projects")
@RequiredArgsConstructor
public class ProjectMeController implements DtoCrudController<ProjectDto, ProjectCreateRequest, ProjectUpdateRequest, Long> {

    private final ProjectService projectService;

    @Override
    public ResponseEntity<ApiResponse<List<ProjectDto>>> listAll() {
        List<ProjectDto> projects = projectService.findAllDto();
        return ResponseEntity.ok(ApiResponse.ok("Proyectos obtenidos exitosamente", projects));
    }

    @Override
    public ResponseEntity<ApiResponse<ProjectDto>> getById(@PathVariable Long id) {
        ProjectDto project = projectService.findDtoById(id);
        return ResponseEntity.ok(ApiResponse.ok("Proyecto obtenido", project));
    }

    @Override
    public ResponseEntity<ApiResponse<ProjectDto>> create(@Valid @RequestBody ProjectCreateRequest create) {
        ProjectDto newProject = projectService.create(create);
        return new ResponseEntity<>(ApiResponse.ok("Proyecto creado exitosamente", newProject), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponse<ProjectDto>> update(@Valid @RequestBody ProjectUpdateRequest updated) {
        ProjectDto updatedProject = projectService.update(updated);
        return ResponseEntity.ok(ApiResponse.ok("Proyecto actualizado exitosamente", updatedProject));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectDto>> update(@PathVariable Long id, @Valid @RequestBody ProjectCreateRequest createData) {
        ProjectUpdateRequest updateRequest = new ProjectUpdateRequest(
                id,
                createData.title(),
                createData.summary(),
                createData.description(),
                createData.repoUrl(),
                createData.liveUrl(),
                createData.coverImage(),
                createData.startDate(),
                createData.endDate(),
                createData.featured(),
                createData.sortOrder()
        );
        ProjectDto updatedProject = projectService.update(updateRequest);
        return ResponseEntity.ok(ApiResponse.ok("Proyecto actualizado exitosamente", updatedProject));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Proyecto eliminado exitosamente"));
    }
}
