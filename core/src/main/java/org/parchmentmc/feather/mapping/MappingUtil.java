package org.parchmentmc.feather.mapping;

import org.parchmentmc.feather.mapping.MappingDataBuilder.MutableClassData;
import org.parchmentmc.feather.mapping.MappingDataBuilder.MutableMethodData;
import org.parchmentmc.feather.mapping.MappingDataContainer.*;

/**
 * Utilities for working with {@link MappingDataContainer}s.
 */
public final class MappingUtil {
    private MappingUtil() {  // Prevent instantiation
    }

    static MappingDataBuilder copyData(MappingDataContainer container) {
        MappingDataBuilder builder = new MappingDataBuilder();

        // Copy packages
        container.getPackages().forEach(pkg -> builder.createPackage(pkg.getName()).addJavadoc(pkg.getJavadoc()));

        // Copy classes
        container.getClasses().forEach(cls -> {
            MutableClassData classData = builder.createClass(cls.getName()).addJavadoc(cls.getJavadoc());

            // Copy fields
            cls.getFields().forEach(field -> classData.createField(field.getName(), field.getDescriptor()).addJavadoc(field.getJavadoc()));

            // Copy methods
            cls.getMethods().forEach(method -> {
                MutableMethodData methodData = classData.createMethod(method.getName(), method.getDescriptor()).addJavadoc(method.getJavadoc());

                // Copy parameters
                method.getParameters().forEach(param -> methodData.createParameter(param.getIndex()).setName(param.getName()).setJavadoc(param.getJavadoc()));
            });
        });

        return builder;
    }


    /**
     * Applies the new mapping data upon the existing base mapping data.
     *
     * <p>All entries from the base mapping data is copied over to the resulting mapping data container.</p>
     *
     * <p>If an entry in the base mapping data has a corresponding entry in the new mapping data, the javadocs (and
     * names for parameters) from the new mapping data entry is used; otherwise, the javadocs (and parameter names)
     * from the base mapping data is used.</p>
     *
     * <p>If a {@link ClassData class entry} from the new mapping data is present in the base mapping data,
     * all new fields and methods (including parameters) from the new mapping data is copied over. Likewise, if
     * a {@link MethodData method entry} in a class from the new mapping data is also present in the same class in the
     * base mapping data, all new {@link ParameterData parameter entries} from the new mapping data is copied over.</p>
     *
     * @param baseData The base mapping data
     * @param newData  The new mapping data
     * @return A new mapping data container which contains the applied mapping data
     */
    public static MappingDataContainer apply(MappingDataContainer baseData, MappingDataContainer newData) {
        if (newData.getClasses().isEmpty() && newData.getPackages().isEmpty()) return baseData;

        MappingDataBuilder builder = new MappingDataBuilder();

        baseData.getPackages().forEach(pkg -> {
            PackageData newPkg = newData.getPackage(pkg.getName());
            builder.createPackage(pkg.getName()).addJavadoc(newPkg != null ? newPkg.getJavadoc() : pkg.getJavadoc());
        });

        baseData.getClasses().forEach(cls -> {
            ClassData newCls = newData.getClass(cls.getName());
            MutableClassData classData = builder.createClass(cls.getName())
                    .addJavadoc(newCls != null ? newCls.getJavadoc() : cls.getJavadoc());

            cls.getFields().forEach(field -> {

                FieldData newField = newCls != null ? newCls.getField(field.getName()) : null;
                classData.createField(field.getName(), field.getDescriptor())
                        .addJavadoc(newField != null ? newField.getJavadoc() : field.getJavadoc());
            });

            cls.getMethods().forEach(method -> {
                MethodData newMethod = newCls != null ? newCls.getMethod(method.getName(), method.getDescriptor()) : null;
                MutableMethodData methodData = classData.createMethod(method.getName(), method.getDescriptor())
                        .addJavadoc(newMethod != null ? newMethod.getJavadoc() : method.getJavadoc());

                method.getParameters().forEach(param -> {
                    ParameterData newParam = newMethod != null ? newMethod.getParameter(param.getIndex()) : null;
                    methodData.createParameter(param.getIndex()).setName(param.getName())
                            .setJavadoc(newParam != null ? newParam.getJavadoc() : param.getJavadoc());
                });
                if (newMethod != null) {
                    newMethod.getParameters().forEach(param -> {
                        if (method.getParameter(param.getIndex()) == null)
                            methodData.createParameter(param.getIndex()).setName(param.getName()).setJavadoc(param.getJavadoc());
                    });
                }
            });

            if (newCls != null) {
                newCls.getFields().forEach(field -> {
                    if (cls.getField(field.getName()) == null)
                        classData.createField(field.getName(), field.getDescriptor()).addJavadoc(field.getJavadoc());
                });

                newCls.getMethods().forEach(method -> {
                    if (cls.getMethod(method.getName(), method.getDescriptor()) == null) {
                        MutableMethodData methodData = classData.createMethod(method.getName(), method.getDescriptor()).addJavadoc(method.getJavadoc());

                        method.getParameters().forEach(t -> methodData.createParameter(t.getIndex()).setName(t.getName()).setJavadoc(t.getJavadoc()));
                    }
                });
            }
        });

        return builder;
    }
}
