package neil.demo.devoxx;

import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IMap;
import com.hazelcast.core.Member;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AddressRestController {
	
	private static ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
    private HazelcastInstance hazelcastInstance;

	@GetMapping("/")
	public Collection<Integer> addresses() {
		log.info("addresses()");
		
		IMap<Integer, Object> addressMap = this.hazelcastInstance.getMap("address");
		
		return new TreeSet<>(addressMap.keySet());
	}
	
	@GetMapping("/{id}")
	public String address(@PathVariable("id") int id) {
		log.info("address({})", id);

		IMap<Integer, Object> addressMap = this.hazelcastInstance.getMap("address");

		// Force reload, get the latest DB copy
		addressMap.evict(id);
		Object result = addressMap.get(id);
		
		try {
			return result == null ? "{}" : objectMapper.writeValueAsString(result);
		} catch (Exception e) {
			log.error(e.getMessage());
			return "{}";
		}
	}

	@GetMapping("toggle")
	public String toggle() {
		log.info("toggle()");

		IExecutorService remoteTaskExecutor = this.hazelcastInstance.getExecutorService("default");

		Callable<Integer> toggle = new Toggle();
		
		final AtomicInteger count = new AtomicInteger(0);
		
		Map<Member, Future<Integer>> executions = remoteTaskExecutor.submitToAllMembers(toggle);
		
		executions.values().forEach(future -> {
			int i;
			try {
				i = future.get();
				while (i-->0) {
					count.incrementAndGet();
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});
		
		return String.valueOf(count);
	}
}
