package neil.demo.devoxx;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringBootTest(classes={MyTestConfig.class})
public class TransactionTRepositoryTest {

    @Autowired(required=true)
    private TransactionTRepository transactionTRepository;

    @Test
    public void findAll() {
            final AtomicInteger count = new AtomicInteger();
            
            this.transactionTRepository.findAll().spliterator().forEachRemaining(thing -> {
                    assertThat("Item:" + count.get(), thing, notNullValue());
                    count.incrementAndGet();
                    log.info(ToStringBuilder.reflectionToString(thing));
            });
            
            assertThat(count.get(), greaterThan(0));
    }
	
}
