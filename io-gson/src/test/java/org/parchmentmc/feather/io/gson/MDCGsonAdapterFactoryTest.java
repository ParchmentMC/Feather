package org.parchmentmc.feather.io.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.manifests.MDCTestConstants;
import org.parchmentmc.feather.mapping.MappingDataContainer;
import org.parchmentmc.feather.mapping.MappingDataContainer.ClassData;
import org.parchmentmc.feather.mapping.MappingDataContainer.FieldData;
import org.parchmentmc.feather.mapping.MappingDataContainer.MethodData;
import org.parchmentmc.feather.mapping.MappingDataContainer.PackageData;

import static org.junit.jupiter.api.Assertions.*;
import static org.parchmentmc.feather.mapping.ImmutableMappingDataContainer.ParameterData;

public class MDCGsonAdapterFactoryTest implements MDCTestConstants {
    private final Gson gson = new GsonBuilder().registerTypeAdapterFactory(new MDCGsonAdapterFactory()).create();

    @Test
    public void testParameters() {
        PARAMETERS.forEach(data -> test(ParameterData.class, data));
    }

    @Test
    public void testMethods() {
        METHODS.forEach(data -> test(MethodData.class, data));
    }

    @Test
    public void testFields() {
        FIELDS.forEach(data -> test(FieldData.class, data));
    }

    @Test
    public void testClasses() {
        CLASSES.forEach(data -> test(ClassData.class, data));
    }

    @Test
    public void testPackages() {
        PACKAGES.forEach(data -> test(PackageData.class, data));
    }

    @Test
    public void testDataContainers() {
        DATA_CONTAINERS.forEach(data -> test(MappingDataContainer.class, data));
    }

    <T> void test(Class<T> typeClass, T original) {
        test(gson.getAdapter(typeClass), original);
    }

    <T> void test(TypeAdapter<T> adapter, T original) {
        final String originalJson = assertDoesNotThrow(() -> adapter.toJson(original));

        final T versionA = assertDoesNotThrow(() -> adapter.fromJson(originalJson));
        assertNotNull(versionA);

        final String versionAJson = assertDoesNotThrow(() -> adapter.toJson(versionA));

        final T versionB = assertDoesNotThrow(() -> adapter.fromJson(versionAJson));
        assertNotNull(versionB);

        assertEquals(original, versionA);
        assertEquals(versionA, versionB);

        assertNotSame(original, versionA);
        assertNotSame(versionA, versionB);

        assertEquals(originalJson, versionAJson);
    }
}
