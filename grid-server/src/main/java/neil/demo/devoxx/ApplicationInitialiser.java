package neil.demo.devoxx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.core.HazelcastInstance;

/**
 * <p>Hazelcast objects are usually created on demand. Create
 * them at start-up instead, so we can see their content in the
 * Management Center and to force RDBMS load.
 * </p>
 */
@Configuration
public class ApplicationInitialiser implements CommandLineRunner {
	
	private static final String[] IMAP_NAMES = {
			"account",
			"jsessionid",
			"transaction"
	};
	
	
	@Autowired
	private HazelcastInstance hazelcastInstance;

	@Override
	public void run(String... args) throws Exception {
        for (String mapName : IMAP_NAMES) {
            this.hazelcastInstance.getMap(mapName);
        }
	}
	
}