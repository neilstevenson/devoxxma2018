package neil.demo.devoxx;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Toggle implements Callable<Integer>, HazelcastInstanceAware, Serializable {
	private static final long serialVersionUID = 1L;

	private HazelcastInstance hazelcastInstance;
	
	@Override
	public Integer call() throws Exception {
		
		IMap<Integer, Object> addressMap = this.hazelcastInstance.getMap("address");
		
		final AtomicInteger count = new AtomicInteger(0);
		
		addressMap.localKeySet().forEach(key -> {
			count.incrementAndGet();
			
			Object value = addressMap.get(key);
			
			log.info("get({},{})", key, value);
			
			if (value instanceof AddressV1) {
				String text = ((AddressV1) value).getLine1();
				((AddressV1) value).setLine1(this.rot13(text));
			}
			if (value instanceof AddressV2) {
				String text = ((AddressV2) value).getLine1();
				((AddressV2) value).setLine1(this.rot13(text));
				text = ((AddressV2) value).getLine2();
				((AddressV2) value).setLine2(this.rot13(text));
			}

			log.info("put({},{})", key, value);
			addressMap.put(key,value);
		});

		return count.get();
	}

	// 13 character rotation in the alphabet. Run once encrypt. Run again decrypt.
	private String rot13(String text) {

        StringBuilder sb = new StringBuilder(text.length());
        for (int i=0; i<text.length() ; i++) {
                char c = text.charAt(i);
                if (c >= 'a' && c <= 'm') {
                        c += 13;
                } else {
                        if (c >= 'n' && c <= 'z' ) {
                                c -= 13;
                        }
                }
                sb.append(c);
        }
        
        return sb.toString();
	}

	@Override
	public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
	}

}
