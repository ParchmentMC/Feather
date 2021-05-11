package org.parchmentmc.feather.mapping;

class MappingUtil {

    public static MappingDataBuilder copyData(MappingDataContainer container) {
        MappingDataBuilder builder = new MappingDataBuilder(container.getFormatVersion());

        // Copy packages
        container.getPackages().forEach(pkg -> builder.createPackage(pkg.getName()).addJavadoc(pkg.getJavadoc()));

        // Copy classes
        container.getClasses().forEach(cls -> {
            MappingDataBuilder.MutableClassData classData = builder.createClass(cls.getName()).addJavadoc(cls.getJavadoc());

            // Copy fields
            cls.getFields().forEach(field -> classData.addField(field.getName(), field.getDescriptor()).addJavadoc(field.getJavadoc()));

            // Copy methods
            cls.getMethods().forEach(method -> {
                MappingDataBuilder.MutableMethodData methodData = classData.addMethod(method.getName(), method.getDescriptor()).addJavadoc(method.getJavadoc());

                // Copy parameters
                method.getParameters().forEach(param -> methodData.createParameter(param.getIndex()).setName(param.getName()).setJavadoc(param.getJavadoc()));
            });
        });

        return builder;
    }
}
