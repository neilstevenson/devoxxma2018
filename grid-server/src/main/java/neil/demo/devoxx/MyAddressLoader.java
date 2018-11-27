package neil.demo.devoxx;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.MapLoader;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MyAddressLoader implements MapLoader<Integer, Object>, ApplicationContextAware {

	private static AddressTRepository addressTRepository;
	private static ObjectMapper objectMapper = new ObjectMapper();

	public Object load(Integer key) {
		log.info("load({})", key);
		
        try {
            Object row = addressTRepository.findById(key).get();
            
        	JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(row));

        	String line1 = "";
        	String line2 = "";

        	Iterator<String> iterator = jsonNode.fieldNames();
        	while (iterator.hasNext()) {
        		String fieldName = iterator.next();
        		
        		switch (fieldName) {
        		case "line1" :
        			JsonNode line1Node = jsonNode.get(fieldName);
        			line1 = line1Node.asText();
        			break;
        		case "line2" :
        			JsonNode line2Node = jsonNode.get(fieldName);
        			line2 = line2Node.asText();
        			break;
        		default:
        		}
        	}
        	
        	AddressV1 addressV1 = new AddressV1(line1);
        	AddressV2 addressV2 = new AddressV2(line1, line2);

        	String v2 = System.getProperty("neil","");

        	return v2.length() > 0 ? addressV2 : addressV1;
        } catch (Exception e) {
            log.error(String.valueOf(key), e);
            return null;
        }		
	}

	public Map<Integer, Object> loadAll(Collection<Integer> keys) {
		log.trace("loadAll({})", keys);
		return new HashMap<>();
	}

	// Don't preload addresses
	public Iterable<Integer> loadAllKeys() {
		log.debug("loadAllKeys()");
		return Collections.emptyList();
	}

	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		addressTRepository = arg0.getBean(AddressTRepository.class);
	}

}
