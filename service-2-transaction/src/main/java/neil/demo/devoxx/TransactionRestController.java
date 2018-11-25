package neil.demo.devoxx;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TransactionRestController {
	
	@Autowired
    private HazelcastInstance hazelcastInstance;

	@GetMapping("/")
	public Collection<Integer> transactions() {
		log.info("transactions()");
		
		IMap<Integer, String> transactionMap = this.hazelcastInstance.getMap("transaction");
		
		return transactionMap.keySet();
	}
	
	@GetMapping("/{id}")
	public String transaction(@PathVariable("id") int id) {
		log.info("transaction({})", id);

		IMap<Integer, String> transactionMap = this.hazelcastInstance.getMap("transaction");

		String result = transactionMap.get(id);
		
		return result == null ? "" : result;
	}

	/**
	 * <p>Cursor style search, retrieve every transaction from thge grid
	 * to this process and compare for a match. This is extremely inefficient.
	 * A better but more complex approach would be to index this field on the
	 * server.
	 * </p>
	 * <p>For account 123, we are looking for this JSON '{@code "accountId":123,}'.
	 * </p>
	 */
	@GetMapping("/accountId/{accountId}")
	public Collection<String> transactionsForAccount(@PathVariable("accountId") int accountId) {
		log.info("transactionsForAccount({})", accountId);
		
		String matchStr = "\"accountId\":" + accountId + ",";

		IMap<Integer, String> transactionMap = this.hazelcastInstance.getMap("transaction");

		Set<String> result = new TreeSet<>();
		
		transactionMap.keySet()
		.stream()
		.forEach(key -> {
			String value = transactionMap.get(key);
			
			if (value.contains(matchStr)) {
				result.add(value);
			}
		});
		
		return result;
	}

}
