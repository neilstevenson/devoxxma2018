package neil.demo.devoxx;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ApplicationInitialiser implements CommandLineRunner {
	
	@Override
	public void run(String... args) throws Exception {
        
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