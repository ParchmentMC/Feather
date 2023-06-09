package org.parchmentmc.feather.io.moshi;

import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.metadata.*;

public class MetadataMoshiAdapterTest extends MoshiTest implements MetadataTestConstants {
    public MetadataMoshiAdapterTest() {
        super(b -> b.add(new SimpleVersionAdapter()).add(new MetadataMoshiAdapter()).add(LinkedHashSetMoshiAdapter.FACTORY));
    }

    @Test
    public void testMethodReferences() {
        METHOD_REFERENCES.forEach(data -> test(Reference.class, data));
    }

    @Test
    public void testMethodMetadata() {
        METHOD_METADATA.forEach(data -> test(MethodMetadata.class, data));
    }

    @Test
    public void testFieldMetadata() {
        FIELD_METADATA.forEach(data -> test(FieldMetadata.class, data));
    }

    @Test
    public void testClassMetadata() {
        CLASS_METADATA.forEach(data -> test(ClassMetadata.class, data));
    }

    @Test
    public void testSourceMetadata() {
        SOURCE_METADATA.forEach(data -> test(SourceMetadata.class, data));
    }
}
