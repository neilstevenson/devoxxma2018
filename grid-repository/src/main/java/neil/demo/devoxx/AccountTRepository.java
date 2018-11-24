package neil.demo.devoxx;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AccountTRepository extends CrudRepository<AccountT, Integer> {
        
        @Query("SELECT a.id FROM AccountT a")
        public Iterable<Integer> findIds();

}
