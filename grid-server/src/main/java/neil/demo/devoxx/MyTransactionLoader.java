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
public class MyTransactionLoader implements MapLoader<String, String>, ApplicationContextAware {

	private static TransactionTRepository transactionTRepository;
	private static ObjectMapper objectMapper = new ObjectMapper();

	public String load(String key) {
		log.trace("load({})", key);

        try {
        	String[] tokens = key.split(",");

        	if (tokens.length==2) {
            	Object o = transactionTRepository.findById(new Integer(tokens[1])).get();
                return objectMapper.writeValueAsString(o);
        	}
        } catch (Exception e) {
            log.error(String.valueOf(key), e);
        }		
        return null;
	}

	public Map<String, String> loadAll(Collection<String> keys) {
		log.trace("loadAll({})", keys);
		Map<String, String> result = new HashMap<>();
		for (String key : keys) {
			String account = this.load(key);
			if (account != null) {
				result.put(key, account);
			}
		}
		return result;
	}

	public Iterable<String> loadAllKeys() {
		log.debug("loadAllKeys()");
		return transactionTRepository.findIds();
	}

	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		transactionTRepository = arg0.getBean(TransactionTRepository.class);
	}

}
