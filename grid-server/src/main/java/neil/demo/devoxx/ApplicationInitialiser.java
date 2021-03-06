package neil.demo.devoxx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.core.HazelcastInstance;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Hazelcast objects are usually created on demand. Create
 * them at start-up instead, so we can see their content in the
 * Management Center and to force RDBMS load.
 * </p>
 */
@Configuration
@Slf4j
public class ApplicationInitialiser implements CommandLineRunner {
	
	private static final String[] IMAP_NAMES = {
			"account",
			"address",
			"jsessionid",
			"transaction"
	};
	private static final String[] IQUEUE_NAMES = {
			"disputes",
	};
	
	
	@Autowired
	private HazelcastInstance hazelcastInstance;

	@Override
	public void run(String... args) throws Exception {
        for (String mapName : IMAP_NAMES) {
            this.hazelcastInstance.getMap(mapName);
        }
        for (String queueName : IQUEUE_NAMES) {
        	this.hazelcastInstance.getQueue(queueName);
        }
        
        AddressV1 addressV1 = new AddressV1();
        AddressV2 addressV2 = new AddressV2();
        
    	String v2 = System.getProperty("neil","");
    	String name = v2.length() > 0 ? addressV2.getClass().getSimpleName() : addressV1.getClass().getSimpleName();
    	int ver = v2.length() > 0 ? addressV2.getClassVersion() : addressV1.getClassVersion();
    	
    	log.info("=============================");
    	log.info("Class version {} for {}", ver, name);
    	log.info("=============================");

	}
	
}