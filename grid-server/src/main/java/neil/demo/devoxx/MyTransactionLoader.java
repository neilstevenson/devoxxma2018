package neil.demo.devoxx;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.MapLoader;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MyTransactionLoader implements MapLoader<Integer, String>, ApplicationContextAware {

	private static TransactionTRepository transactionTRepository;
	private static ObjectMapper objectMapper = new ObjectMapper();

	public String load(Integer key) {
		log.trace("load({})", key);
		
        try {
        	Object o = transactionTRepository.findById(key).get();
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            log.error(String.valueOf(key), e);
            return null;
        }		
	}

	public Map<Integer, String> loadAll(Collection<Integer> keys) {
		log.trace("loadAll({})", keys);
		Map<Integer, String> result = new HashMap<>();
		for (Integer key : keys) {
			String account = this.load(key);
			if (account != null) {
				result.put(key, account);
			}
		}
		return result;
	}

	public Iterable<Integer> loadAllKeys() {
		log.debug("loadAllKeys()");
		return transactionTRepository.findIds();
	}

	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		transactionTRepository = arg0.getBean(TransactionTRepository.class);
	}

}
