package org.parchmentmc.feather.manifests;

import org.parchmentmc.feather.mapping.ImmutableMappingDataContainer;
import org.parchmentmc.feather.mapping.MappingDataContainer;
import org.parchmentmc.feather.util.SimpleVersion;

import java.util.List;

import static com.google.common.collect.ImmutableList.of;
import static org.parchmentmc.feather.mapping.MappingDataContainer.CURRENT_FORMAT;

/**
 * Constants of {@link MappingDataContainer} and inner classes, for use in tests.
 */
public interface MDCTestConstants {
    List<MappingDataContainer.ParameterData> PARAMETERS = of(
            new ImmutableMappingDataContainer.ImmutableParameterData((byte) 1, "1testName", "1Some javadoc"),
            new ImmutableMappingDataContainer.ImmutableParameterData((byte) 2, null, "2Some javadoc"),
            new ImmutableMappingDataContainer.ImmutableParameterData((byte) 3, "3testName", null),
            new ImmutableMappingDataContainer.ImmutableParameterData((byte) 4, null, null)
    );

    List<String> MULTILINE_JAVADOC = of("Some javadocs", "", "Multiline javadocs!");

    List<MappingDataContainer.MethodData> METHODS = of(
            new ImmutableMappingDataContainer.ImmutableMethodData("<init>", "(Ljava/lang/String;BBZ)V", of(), PARAMETERS),
            new ImmutableMappingDataContainer.ImmutableMethodData("test2", "(IIIS)Z", MULTILINE_JAVADOC, PARAMETERS),
            new ImmutableMappingDataContainer.ImmutableMethodData("<clinit>", "(V", of(), of()),
            new ImmutableMappingDataContainer.ImmutableMethodData("isTestable", "()Z", MULTILINE_JAVADOC, of())
    );

    List<MappingDataContainer.FieldData> FIELDS = of(
            new ImmutableMappingDataContainer.ImmutableFieldData("aField", "F", of()),
            new ImmutableMappingDataContainer.ImmutableFieldData("anotherOne", "Ljava/util/Objects;", of()),
            new ImmutableMappingDataContainer.ImmutableFieldData("thirdTimes", "Z", MULTILINE_JAVADOC)
    );

    List<MappingDataContainer.ClassData> CLASSES = of(
            new ImmutableMappingDataContainer.ImmutableClassData("ClassOne", of(), FIELDS, METHODS),
            new ImmutableMappingDataContainer.ImmutableClassData("com/example/ClassTwo", of(), FIELDS, METHODS),
            new ImmutableMappingDataContainer.ImmutableClassData("com/example/ClassTwo$Inner", MULTILINE_JAVADOC, FIELDS, METHODS),
            new ImmutableMappingDataContainer.ImmutableClassData("com/example/test/ClassyClass", MULTILINE_JAVADOC, FIELDS, METHODS),
            new ImmutableMappingDataContainer.ImmutableClassData("a/ClassOne", of(), FIELDS, of()),
            new ImmutableMappingDataContainer.ImmutableClassData("a/com/example/ClassTwo", of(), FIELDS, of()),
            new ImmutableMappingDataContainer.ImmutableClassData("a/com/example/ClassTwo$Inner", MULTILINE_JAVADOC, FIELDS, of()),
            new ImmutableMappingDataContainer.ImmutableClassData("a/com/example/test/ClassyClass", MULTILINE_JAVADOC, FIELDS, of()),
            new ImmutableMappingDataContainer.ImmutableClassData("b/ClassOne", of(), of(), METHODS),
            new ImmutableMappingDataContainer.ImmutableClassData("b/com/example/ClassTwo", of(), of(), METHODS),
            new ImmutableMappingDataContainer.ImmutableClassData("b/com/example/ClassTwo$Inner", MULTILINE_JAVADOC, of(), METHODS),
            new ImmutableMappingDataContainer.ImmutableClassData("b/com/example/test/ClassyClass", MULTILINE_JAVADOC, of(), METHODS),
            new ImmutableMappingDataContainer.ImmutableClassData("c/ClassOne", of(), of(), of()),
            new ImmutableMappingDataContainer.ImmutableClassData("c/com/example/ClassTwo", of(), of(), of()),
            new ImmutableMappingDataContainer.ImmutableClassData("c/com/example/ClassTwo$Inner", MULTILINE_JAVADOC, of(), of()),
            new ImmutableMappingDataContainer.ImmutableClassData("c/com/example/test/ClassyClass", MULTILINE_JAVADOC, of(), of())
    );

    List<MappingDataContainer.PackageData> PACKAGES = of(
            new ImmutableMappingDataContainer.ImmutablePackageData("com/example", of()),
            new ImmutableMappingDataContainer.ImmutablePackageData("com/example/test", MULTILINE_JAVADOC)
    );

    List<MappingDataContainer> DATA_CONTAINERS = of(
            new ImmutableMappingDataContainer(CURRENT_FORMAT, of(), of()),
            new ImmutableMappingDataContainer(CURRENT_FORMAT, PACKAGES, of()),
            new ImmutableMappingDataContainer(CURRENT_FORMAT, of(), CLASSES),
            new ImmutableMappingDataContainer(SimpleVersion.of("1.0.1"), PACKAGES, CLASSES)
    );
}
