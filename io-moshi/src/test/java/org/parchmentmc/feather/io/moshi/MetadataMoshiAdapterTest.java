package org.parchmentmc.feather.io.moshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.manifests.MetadataTestConstants;
import org.parchmentmc.feather.metadata.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

public class MetadataMoshiAdapterTest extends MoshiTest implements MetadataTestConstants {
    public MetadataMoshiAdapterTest() {
        super(b -> b.add(new SimpleVersionAdapter()).add(new MetadataMoshiAdapter()).add(LinkedHashSetMoshiAdapter.FACTORY));
    }

    @Test
    public void testMethodReferences() {
        METHOD_REFERENCES.forEach(data -> test(MethodReference.class, data));
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
