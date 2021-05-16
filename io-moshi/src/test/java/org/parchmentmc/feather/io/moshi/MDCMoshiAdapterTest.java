package org.parchmentmc.feather.io.moshi;

import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.manifests.MDCTestConstants;
import org.parchmentmc.feather.mapping.MappingDataContainer;
import org.parchmentmc.feather.mapping.MappingDataContainer.ClassData;
import org.parchmentmc.feather.mapping.MappingDataContainer.FieldData;
import org.parchmentmc.feather.mapping.MappingDataContainer.MethodData;
import org.parchmentmc.feather.mapping.MappingDataContainer.PackageData;

import static org.parchmentmc.feather.mapping.MappingDataContainer.ParameterData;

public class MDCMoshiAdapterTest extends MoshiTest implements MDCTestConstants {
    public MDCMoshiAdapterTest() {
        super(b -> b.add(new MDCMoshiAdapter()));
    }

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
}
