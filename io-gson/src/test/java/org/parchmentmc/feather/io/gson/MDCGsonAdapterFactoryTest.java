package org.parchmentmc.feather.io.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.mapping.ImmutableMappingDataContainer;
import org.parchmentmc.feather.mapping.ImmutableMappingDataContainer.ImmutableClassData;
import org.parchmentmc.feather.mapping.ImmutableMappingDataContainer.ImmutableFieldData;
import org.parchmentmc.feather.mapping.ImmutableMappingDataContainer.ImmutableMethodData;
import org.parchmentmc.feather.mapping.ImmutableMappingDataContainer.ImmutablePackageData;
import org.parchmentmc.feather.mapping.MappingDataContainer;
import org.parchmentmc.feather.mapping.MappingDataContainer.ClassData;
import org.parchmentmc.feather.mapping.MappingDataContainer.FieldData;
import org.parchmentmc.feather.mapping.MappingDataContainer.MethodData;
import org.parchmentmc.feather.mapping.MappingDataContainer.PackageData;
import org.parchmentmc.feather.util.SimpleVersion;

import java.util.List;

import static com.google.common.collect.ImmutableList.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.parchmentmc.feather.mapping.ImmutableMappingDataContainer.ImmutableParameterData;
import static org.parchmentmc.feather.mapping.ImmutableMappingDataContainer.ParameterData;
import static org.parchmentmc.feather.mapping.MappingDataContainer.CURRENT_FORMAT;

public class MDCGsonAdapterFactoryTest {
    private final Gson gson = new GsonBuilder().registerTypeAdapterFactory(new MDCGsonAdapterFactory()).create();

    static final List<ParameterData> PARAMETERS = of(
            new ImmutableParameterData((byte) 1, "1testName", "1Some javadoc"),
            new ImmutableParameterData((byte) 2, null, "2Some javadoc"),
            new ImmutableParameterData((byte) 3, "3testName", null),
            new ImmutableParameterData((byte) 4, null, null)
    );

    static final List<String> MULTILINE_JAVADOC = of("Some javadocs", "", "Multiline javadocs!");

    static final List<MethodData> METHODS = of(
            new ImmutableMethodData("<init>", "(Ljava/lang/String;BBZ)V", of(), PARAMETERS),
            new ImmutableMethodData("test2", "(IIIS)Z", MULTILINE_JAVADOC, PARAMETERS),
            new ImmutableMethodData("<clinit>", "(V", of(), of()),
            new ImmutableMethodData("isTestable", "()Z", MULTILINE_JAVADOC, of())
    );

    static final List<FieldData> FIELDS = of(
            new ImmutableFieldData("aField", "F", of()),
            new ImmutableFieldData("anotherOne", "Ljava/util/Objects;", of()),
            new ImmutableFieldData("thirdTimes", "Z", MULTILINE_JAVADOC)
    );

    static final List<ClassData> CLASSES = of(
            new ImmutableClassData("ClassOne", of(), FIELDS, METHODS),
            new ImmutableClassData("com/example/ClassTwo", of(), FIELDS, METHODS),
            new ImmutableClassData("com/example/ClassTwo$Inner", MULTILINE_JAVADOC, FIELDS, METHODS),
            new ImmutableClassData("com/example/test/ClassyClass", MULTILINE_JAVADOC, FIELDS, METHODS),
            new ImmutableClassData("a/ClassOne", of(), FIELDS, of()),
            new ImmutableClassData("a/com/example/ClassTwo", of(), FIELDS, of()),
            new ImmutableClassData("a/com/example/ClassTwo$Inner", MULTILINE_JAVADOC, FIELDS, of()),
            new ImmutableClassData("a/com/example/test/ClassyClass", MULTILINE_JAVADOC, FIELDS, of()),
            new ImmutableClassData("b/ClassOne", of(), of(), METHODS),
            new ImmutableClassData("b/com/example/ClassTwo", of(), of(), METHODS),
            new ImmutableClassData("b/com/example/ClassTwo$Inner", MULTILINE_JAVADOC, of(), METHODS),
            new ImmutableClassData("b/com/example/test/ClassyClass", MULTILINE_JAVADOC, of(), METHODS),
            new ImmutableClassData("c/ClassOne", of(), of(), of()),
            new ImmutableClassData("c/com/example/ClassTwo", of(), of(), of()),
            new ImmutableClassData("c/com/example/ClassTwo$Inner", MULTILINE_JAVADOC, of(), of()),
            new ImmutableClassData("c/com/example/test/ClassyClass", MULTILINE_JAVADOC, of(), of())
    );

    static final List<PackageData> PACKAGES = of(
            new ImmutablePackageData("com/example", of()),
            new ImmutablePackageData("com/example/test", MULTILINE_JAVADOC)
    );

    static final List<MappingDataContainer> DATA_CONTAINERS = of(
            new ImmutableMappingDataContainer(CURRENT_FORMAT, of(), of()),
            new ImmutableMappingDataContainer(CURRENT_FORMAT, PACKAGES, of()),
            new ImmutableMappingDataContainer(CURRENT_FORMAT, of(), CLASSES),
            new ImmutableMappingDataContainer(SimpleVersion.of("1.0.1"), PACKAGES, CLASSES)
    );

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
