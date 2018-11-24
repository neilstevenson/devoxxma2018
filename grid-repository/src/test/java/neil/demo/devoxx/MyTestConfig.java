package neil.demo.devoxx;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {"neil.demo.devoxx"})
@EntityScan(basePackages = {"neil.demo.devoxx"})
public class MyTestConfig {

}
