package org.parchmentmc.feather.io.tests;

import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.manifests.MDCTestConstants;
import org.parchmentmc.feather.mapping.MappingDataContainer;
import org.parchmentmc.feather.mapping.VersionedMappingDataContainer;

public class MDCAdapterTest extends RoundRobinTest implements MDCTestConstants {

    @Test
    public void testParameters() {
        PARAMETERS.forEach(data -> test(MappingDataContainer.ParameterData.class, data));
    }

    @Test
    public void testMethods() {
        METHODS.forEach(data -> test(MappingDataContainer.MethodData.class, data));
    }

    @Test
    public void testFields() {
        FIELDS.forEach(data -> test(MappingDataContainer.FieldData.class, data));
    }

    @Test
    public void testClasses() {
        CLASSES.forEach(data -> test(MappingDataContainer.ClassData.class, data));
    }

    @Test
    public void testPackages() {
        PACKAGES.forEach(data -> test(MappingDataContainer.PackageData.class, data));
    }

    @Test
    public void testDataContainers() {
        DATA_CONTAINERS.forEach(data -> test(VersionedMappingDataContainer.class, data));
    }

}
