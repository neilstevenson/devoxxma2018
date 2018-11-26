package neil.demo.devoxx;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TransactionTRepository extends CrudRepository<TransactionT, Integer> {
        
	// Hazelcast key is compound of database foreign and primary
    @Query("SELECT CONCAT(t.accountId, ',', t.id) FROM TransactionT t")
    public Iterable<String> findIds();

}
