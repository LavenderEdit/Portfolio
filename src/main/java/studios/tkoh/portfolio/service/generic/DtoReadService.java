package studios.tkoh.portfolio.service.generic;

import java.util.List;

/**
 *
 * @author Studios TKOH!
 * @param <D1> DTO 1 -> The one that returns the data
 * @param <ID> ID -> The type of ID from the model/class
 */
public interface DtoReadService<D1, ID> {

    D1 findDtoById(ID id);

    List<D1> findAllDto();
}
