package studios.tkoh.portfolio.service.generic;

/**
 *
 * @author Studios TKOH!
 * @param <D1> DTO 1 -> The one that returns the data
 * @param <D2> DTO 2 -> The one that is used to create
 * @param <D3> DTO 3 -> The one that is used to update (in some special cases it
 * may be used only the one that returns the data if they are very similar)
 * @param <ID> ID -> The type of ID from the model/class
 */
public interface DtoWriteService<D1, D2, D3, ID> {

    D1 create(D2 create);

    D1 update(D3 updated);

    boolean delete(ID id);
}
