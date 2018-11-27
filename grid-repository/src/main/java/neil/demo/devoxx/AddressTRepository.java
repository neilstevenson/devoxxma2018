package neil.demo.devoxx;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AddressTRepository extends CrudRepository<AddressT, Integer> {
        
        @Query("SELECT a.id FROM AddressT a")
        public Iterable<Integer> findIds();

}
