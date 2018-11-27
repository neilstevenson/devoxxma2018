package neil.demo.devoxx;

import java.io.IOException;

import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;
import com.hazelcast.nio.serialization.VersionedPortable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class AddressV1 implements VersionedPortable {

    private static final String FIELD_LINE1 = "line1";
    
    private String line1;

    @Override
	public int getClassVersion() {
		return 1;
    }

	@Override
	public int getClassId() {
		return 66;
    }

	@Override
	public int getFactoryId() {
		return 99;
    }

	@Override
	public void readPortable(PortableReader portableReader) throws IOException {
		this.line1 = portableReader.readUTF(FIELD_LINE1);
		log.info("{} readPortable() {}", this.getClass().getSimpleName(), this);
    }

    @Override
    public void writePortable(PortableWriter portableWriter) throws IOException {
    	log.info("{} writePortable() {}", this.getClass().getSimpleName(), this);
    	portableWriter.writeUTF(FIELD_LINE1, this.line1);
    }
        
}
