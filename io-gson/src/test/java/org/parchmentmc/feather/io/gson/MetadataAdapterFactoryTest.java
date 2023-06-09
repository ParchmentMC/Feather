package org.parchmentmc.feather.io.gson;

import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.io.gson.metadata.MetadataAdapterFactory;
import org.parchmentmc.feather.metadata.*;
import org.parchmentmc.feather.util.SimpleVersion;

public class MetadataAdapterFactoryTest extends GSONTest implements MetadataTestConstants {
    public MetadataAdapterFactoryTest() {
        super(b -> b.registerTypeAdapter(SimpleVersion.class, new SimpleVersionAdapter())
                .registerTypeAdapterFactory(new MetadataAdapterFactory()));
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
