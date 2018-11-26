package neil.demo.devoxx;

import java.util.Collection;
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
public class AccountRestController {
	
	@Autowired
    private HazelcastInstance hazelcastInstance;

	@GetMapping("/")
	public Collection<Integer> accounts() {
		log.info("accounts()");
		
		IMap<Integer, String> accountMap = this.hazelcastInstance.getMap("account");
		
		return new TreeSet<>(accountMap.keySet());
	}
	
	@GetMapping("/{id}")
	public String account(@PathVariable("id") int id) {
		log.info("account({})", id);

		IMap<Integer, String> accountMap = this.hazelcastInstance.getMap("account");

		String result = accountMap.get(id);
		
		return result == null ? "" : result;
	}

}
