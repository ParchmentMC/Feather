package org.parchmentmc.feather.mapping;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

/**
 * An immutable {@link MappingDataContainer}.
 */
public class ImmutableMappingDataContainer implements MappingDataContainer {
    private final Set<PackageData> packages;
    private final Map<String, PackageData> packagesMap;

    private final Set<ClassData> classes;
    private final Map<String, ClassData> classesMap;

    public ImmutableMappingDataContainer(Collection<? extends PackageData> packages, Collection<? extends ClassData> classes) {
        this.packages = ImmutableSortedSet.copyOf(PackageData.COMPARATOR, packages);
        this.classes = ImmutableSortedSet.copyOf(ClassData.COMPARATOR, classes);
        this.packagesMap = Maps.uniqueIndex(this.packages, PackageData::getName);
        this.classesMap = Maps.uniqueIndex(this.classes, ClassData::getName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<? extends PackageData> getPackages() {
        return packages;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public PackageData getPackage(String packageName) {
        return packagesMap.get(packageName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<? extends ClassData> getClasses() {
        return classes;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public ClassData getClass(String className) {
        return classesMap.get(className);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MappingDataContainer)) return false;
        MappingDataContainer builder = (MappingDataContainer) o;
        return getPackages().equals(builder.getPackages()) && getClasses().equals(builder.getClasses());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPackages(), getClasses());
    }

    /**
     * An immutable {@link MappingDataContainer.PackageData}.
     */
    public static class ImmutablePackageData implements MappingDataContainer.PackageData {
        private final String name;
        private final List<String> javadoc;

        public ImmutablePackageData(String name, List<String> javadoc) {
            this.name = name;
            this.javadoc = ImmutableList.copyOf(javadoc);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName() {
            return name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public List<String> getJavadoc() {
            return javadoc;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PackageData)) return false;
            PackageData that = (PackageData) o;
            return Objects.equals(getName(), that.getName()) && getJavadoc().equals(that.getJavadoc());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName(), getJavadoc());
        }
    }

    /**
     * An immutable {@link MappingDataContainer.ClassData}.
     */
    public static class ImmutableClassData implements MappingDataContainer.ClassData {
        private final String name;
        private final List<String> javadoc;

        private final Set<FieldData> fields;
        private final Map<String, FieldData> fieldsMap;

        private final Set<MethodData> methods;
        private final Map<String, MethodData> methodsMap;

        public ImmutableClassData(String name, List<String> javadoc, Collection<? extends FieldData> fields, Collection<? extends MethodData> methods) {
            this.name = name;
            this.javadoc = ImmutableList.copyOf(javadoc);
            this.fields = ImmutableSortedSet.copyOf(FieldData.COMPARATOR, fields);
            this.methods = ImmutableSortedSet.copyOf(MethodData.COMPARATOR, methods);
            this.fieldsMap = Maps.uniqueIndex(this.fields, FieldData::getName);
            this.methodsMap = Maps.uniqueIndex(this.methods, this::dataToKey);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName() {
            return name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public List<String> getJavadoc() {
            return javadoc;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Collection<? extends FieldData> getFields() {
            return fields;
        }

        /**
         * {@inheritDoc}
         */
        @Nullable
        @Override
        public FieldData getField(String fieldName) {
            return fieldsMap.get(fieldName);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Collection<? extends MethodData> getMethods() {
            return methods;
        }

        /**
         * {@inheritDoc}
         */
        @Nullable
        @Override
        public MethodData getMethod(String methodName, String descriptor) {
            return methodsMap.get(key(methodName, descriptor));
        }

        private String key(String name, String descriptor) {
            return name + ":" + descriptor;
        }

        private String dataToKey(MappingDataContainer.MethodData method) {
            return key(method.getName(), method.getDescriptor());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ClassData)) return false;
            ClassData that = (ClassData) o;
            return Objects.equals(getName(), that.getName()) && getJavadoc().equals(that.getJavadoc())
                    && getFields().equals(that.getFields()) && getMethods().equals(that.getMethods());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName(), getJavadoc(), getFields(), getMethods());
        }
    }

    /**
     * An immutable {@link MappingDataContainer.FieldData}.
     */
    public static class ImmutableFieldData implements MappingDataContainer.FieldData {
        private final String name;
        private final String descriptor;
        private final List<String> javadoc;
        @Nullable
        private final ConstantData constant;

        public ImmutableFieldData(String name, String descriptor, List<String> javadoc, @Nullable ConstantData constant) {
            this.name = name;
            this.descriptor = descriptor;
            this.javadoc = ImmutableList.copyOf(javadoc);
            this.constant = constant;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName() {
            return name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDescriptor() {
            return descriptor;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public List<String> getJavadoc() {
            return javadoc;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ConstantData getConstant() {
            return constant;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ImmutableFieldData)) return false;

            ImmutableFieldData that = (ImmutableFieldData) o;

            if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
            if (getDescriptor() != null ? !getDescriptor().equals(that.getDescriptor()) : that.getDescriptor() != null)
                return false;
            if (getJavadoc() != null ? !getJavadoc().equals(that.getJavadoc()) : that.getJavadoc() != null)
                return false;
            return getConstant() != null ? getConstant().equals(that.getConstant()) : that.getConstant() == null;
        }

        @Override
        public int hashCode() {
            int result = getName() != null ? getName().hashCode() : 0;
            result = 31 * result + (getDescriptor() != null ? getDescriptor().hashCode() : 0);
            result = 31 * result + (getJavadoc() != null ? getJavadoc().hashCode() : 0);
            result = 31 * result + (getConstant() != null ? getConstant().hashCode() : 0);
            return result;
        }
    }

    /**
     * An immutable {@link MappingDataContainer.MethodData}.
     */
    public static class ImmutableMethodData implements MappingDataContainer.MethodData {
        private final String name;
        private final String descriptor;
        private final List<String> javadoc;
        private final Set<ParameterData> parameters;
        private final Map<Byte, ParameterData> parametersMap;

        public ImmutableMethodData(String name, String descriptor, List<String> javadoc, Collection<? extends ParameterData> parameters) {
            this.name = name;
            this.javadoc = ImmutableList.copyOf(javadoc);
            this.descriptor = descriptor;
            this.parameters = ImmutableSortedSet.copyOf(ParameterData.COMPARATOR, parameters);
            this.parametersMap = Maps.uniqueIndex(this.parameters, ParameterData::getIndex);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName() {
            return name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDescriptor() {
            return descriptor;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public List<String> getJavadoc() {
            return javadoc;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Collection<? extends ParameterData> getParameters() {
            return parameters;
        }

        /**
         * {@inheritDoc}
         */
        @Nullable
        @Override
        public ParameterData getParameter(byte index) {
            return parametersMap.get(index);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MethodData)) return false;
            MethodData that = (MethodData) o;
            return getName().equals(that.getName()) && getDescriptor().equals(that.getDescriptor())
                    && getJavadoc().equals(that.getJavadoc()) && getParameters().equals(that.getParameters());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName(), getDescriptor(), getJavadoc(), getParameters());
        }
    }

    /**
     * An immutable {@link MappingDataContainer.ParameterData}.
     */
    public static class ImmutableParameterData implements MappingDataContainer.ParameterData {
        private final byte index;
        @Nullable
        private final String name;
        @Nullable
        private final String javadoc;
        @Nullable
        private final ConstantData constant;

        public ImmutableParameterData(byte index, @Nullable String name, @Nullable String javadoc, @Nullable ConstantData constant) {
            this.index = index;
            this.name = name;
            this.javadoc = javadoc;
            this.constant = constant;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public byte getIndex() {
            return index;
        }

        /**
         * {@inheritDoc}
         */
        @Nullable
        @Override
        public String getName() {
            return name;
        }

        /**
         * {@inheritDoc}
         */
        @Nullable
        @Override
        public String getJavadoc() {
            return javadoc;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ConstantData getConstant() {
            return constant;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ImmutableParameterData)) return false;

            ImmutableParameterData that = (ImmutableParameterData) o;

            if (getIndex() != that.getIndex()) return false;
            if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
            if (getJavadoc() != null ? !getJavadoc().equals(that.getJavadoc()) : that.getJavadoc() != null)
                return false;
            return getConstant() != null ? getConstant().equals(that.getConstant()) : that.getConstant() == null;
        }

        @Override
        public int hashCode() {
            int result = getIndex();
            result = 31 * result + (getName() != null ? getName().hashCode() : 0);
            result = 31 * result + (getJavadoc() != null ? getJavadoc().hashCode() : 0);
            result = 31 * result + (getConstant() != null ? getConstant().hashCode() : 0);
            return result;
        }
    }

    /**
     * An immutable {@link MappingDataContainer.ConstantData}.
     */
    public static final class ImmutableConstantData implements ConstantData {

        private final ConstantType type;
        private final List<ConstantValueData> values;

        public ImmutableConstantData(ConstantType type, List<ConstantValueData> values) {
            this.type = type;
            this.values = values;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ConstantType getType() {
            return type;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public List<ConstantValueData> getValues() {
            return values;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ImmutableConstantData)) return false;

            ImmutableConstantData that = (ImmutableConstantData) o;

            if (getType() != that.getType()) return false;
            return getValues().equals(that.getValues());
        }

        @Override
        public int hashCode() {
            int result = getType().hashCode();
            result = 31 * result + getValues().hashCode();
            return result;
        }
    }

    /**
     * An immutable {@link MappingDataContainer.ConstantValueData}.
     */
    public static final class ImmutableConstantValueData implements ConstantValueData {

        private final int value;
        private final String reference;

        public ImmutableConstantValueData(int value, String reference) {
            this.value = value;
            this.reference = reference;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getValue() {
            return value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getReference() {
            return reference;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ImmutableConstantValueData)) return false;

            ImmutableConstantValueData that = (ImmutableConstantValueData) o;

            if (getValue() != that.getValue()) return false;
            return getReference().equals(that.getReference());
        }

        @Override
        public int hashCode() {
            int result = getValue();
            result = 31 * result + getReference().hashCode();
            return result;
        }
    }
}
