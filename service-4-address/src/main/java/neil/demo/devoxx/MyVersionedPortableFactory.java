package neil.demo.devoxx;

import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableFactory;

public class MyVersionedPortableFactory implements PortableFactory {

        @Override
        public Portable create(int i) {
                if (i==66) {
                	String v2 = System.getProperty("neil","");

                	return v2.length() > 0 ? new AddressV2() : new AddressV1();
                }
                return null;
        }

}
