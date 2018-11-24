package neil.demo.devoxx;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TransactionTRepository extends CrudRepository<TransactionT, Integer> {
        
        @Query("SELECT t.id FROM TransactionT t")
        public Iterable<Integer> findIds();

}
