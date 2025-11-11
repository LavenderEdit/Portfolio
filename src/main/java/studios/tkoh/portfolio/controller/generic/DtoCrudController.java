package studios.tkoh.portfolio.controller.generic;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import studios.tkoh.portfolio.dto.response.ApiResponse;

/**
 *
 * @author Studios TKOH!
 * @param <D1> DTO 1 -> The one that returns the data
 * @param <D2> DTO 2 -> The one that is used to create
 * @param <D3> DTO 3 -> The one that is used to update (in some special cases it
 * may be used only the one that returns the data if they are very similar)
 * @param <ID> ID -> The type of ID from the model/class
 */
public interface DtoCrudController<D1, D2, D3, ID> {

    @GetMapping
    ResponseEntity<ApiResponse<List<D1>>> listAll();

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<D1>> getById(@PathVariable ID id);

    @PostMapping
    ResponseEntity<ApiResponse<D1>> create(@RequestBody D2 create);

    @PutMapping
    ResponseEntity<ApiResponse<D1>> update(@RequestBody D3 updated);

    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<Void>> delete(@PathVariable ID id);
}
