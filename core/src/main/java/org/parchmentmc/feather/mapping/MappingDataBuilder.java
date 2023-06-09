package org.parchmentmc.feather.mapping;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

/**
 * A mutable builder implementation of {@link MappingDataContainer}.
 */
public class MappingDataBuilder implements MappingDataContainer {
    private final Set<MutablePackageData> packages = new TreeSet<>(PackageData.COMPARATOR);
    private transient final Map<String, MutablePackageData> packagesMap = new HashMap<>();
    private transient final Collection<MutablePackageData> packagesView = Collections.unmodifiableSet(packages);
    private final Set<MutableClassData> classes = new TreeSet<>(ClassData.COMPARATOR);
    private transient final Map<String, MutableClassData> classesMap = new HashMap<>();
    private transient final Collection<MutableClassData> classesView = Collections.unmodifiableSet(classes);

    public static MappingDataBuilder copyOf(MappingDataContainer data) {
        return MappingUtil.copyData(data);
    }

    public MappingDataBuilder() {
    }

    @Override
    public Collection<MutablePackageData> getPackages() {
        return packagesView;
    }

    @Nullable
    @Override
    public MutablePackageData getPackage(String packageName) {
        return packagesMap.get(packageName);
    }

    public MutablePackageData createPackage(String packageName) {
        MutablePackageData pkg = makePackage(packageName);
        packagesMap.put(packageName, pkg);
        return pkg;
    }

    public MutablePackageData getOrCreatePackage(String packageName) {
        return packagesMap.computeIfAbsent(packageName, this::makePackage);
    }

    private MutablePackageData makePackage(String packageName) {
        MutablePackageData pkg = new MutablePackageData(packageName);
        packages.add(pkg);
        return pkg;
    }

    public boolean removePackage(String packageName) {
        MutablePackageData data = packagesMap.remove(packageName);
        if (data == null) return false;
        packages.remove(data);
        return true;
    }

    public MappingDataBuilder clearPackages() {
        packages.clear();
        packagesMap.clear();
        return this;
    }

    @Override
    public Collection<? extends MutableClassData> getClasses() {
        return classesView;
    }

    @Nullable
    @Override
    public MutableClassData getClass(String className) {
        return classesMap.get(className);
    }

    public MutableClassData createClass(String className) {
        MutableClassData cls = makeClass(className);
        classesMap.put(className, cls);
        return cls;
    }

    public MutableClassData getOrCreateClass(String className) {
        return classesMap.computeIfAbsent(className, this::makeClass);
    }

    private MutableClassData makeClass(String className) {
        MutableClassData cls = new MutableClassData(className);
        classes.add(cls);
        return cls;
    }

    public boolean removeClass(String className) {
        MutableClassData data = classesMap.remove(className);
        if (data == null) return false;
        classes.remove(data);
        return true;
    }

    public MappingDataBuilder clearClasses() {
        classes.clear();
        classesMap.clear();
        return this;
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

    public interface MutableHasJavadoc<T> {
        default T addJavadoc(String... line) {
            return addJavadoc(Arrays.asList(line));
        }

        T addJavadoc(Collection<? extends String> lines);

        T clearJavadoc();
    }

    public interface MutableHasConstants<T> extends MutableHasConstantValues<T>, CanContainConstantValue {
        @Override
        @Nullable
        MutableConstantData getConstant();

        T setConstant(@Nullable ConstantData constantData);

        @Override
        default List<ConstantValueData> getConstantValues() {
            final MappingDataContainer.ConstantData constantData = this.getConstant();
            if (constantData == null) {
                setConstant(new MutableConstantData());
                return getConstantValues();
            }

            return constantData.getValues();
        }
    }

    public interface MutableHasConstantValues<T> {
        default T addConstantValue(ConstantValueData value) {
            final List<ConstantValueData> values = this.getConstantValues();
            if (!values.contains(value)) {
                values.add(value);
            }
            return setConstantValues(values);
        }

        default T removeConstantValue(ConstantValueData value) {
            final List<ConstantValueData> values = this.getConstantValues();
            values.remove(value);
            return setConstantValues(values);
        }

        default T addConstantValue(int value, String reference) {
            return addConstantValue(new MutableConstantValueData(value, reference));
        }

        default T removeConstantValue(int value, String reference) {
            return removeConstantValue(new MutableConstantValueData(value, reference));
        }

        List<ConstantValueData> getConstantValues();

        T setConstantValues(List<ConstantValueData> values);
    }

    public static class MutablePackageData implements MappingDataContainer.PackageData, MutableHasJavadoc<MutablePackageData> {
        private final String name;
        private final List<String> javadoc = new ArrayList<>();
        private transient final List<String> javadocView = Collections.unmodifiableList(javadoc);

        MutablePackageData(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public List<String> getJavadoc() {
            return javadocView;
        }

        @Override
        public MutablePackageData addJavadoc(Collection<? extends String> lines) {
            javadoc.addAll(lines);
            return this;
        }

        @Override
        public MutablePackageData clearJavadoc() {
            javadoc.clear();
            return this;
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

    public static class MutableClassData implements MappingDataContainer.ClassData, MutableHasJavadoc<MutableClassData> {
        private final String name;
        private final List<String> javadoc = new ArrayList<>();
        private transient final List<String> javadocView = Collections.unmodifiableList(javadoc);

        private final Set<MutableFieldData> fields = new TreeSet<>(FieldData.COMPARATOR);
        private transient final Map<String, MutableFieldData> fieldsMap = new HashMap<>();
        private transient final Collection<MutableFieldData> fieldsView = Collections.unmodifiableSet(fields);

        // Keys for method map is '<name>:<descriptor>'
        private final Set<MutableMethodData> methods = new TreeSet<>(MethodData.COMPARATOR);
        private transient final Map<String, MutableMethodData> methodsMap = new HashMap<>();
        private transient final Collection<MutableMethodData> methodsView = Collections.unmodifiableSet(methods);

        MutableClassData(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public List<String> getJavadoc() {
            return javadocView;
        }

        @Override
        public MutableClassData addJavadoc(Collection<? extends String> lines) {
            javadoc.addAll(lines);
            return this;
        }

        @Override
        public MutableClassData clearJavadoc() {
            javadoc.clear();
            return this;
        }

        @Override
        public Collection<MutableFieldData> getFields() {
            return fieldsView;
        }

        @Nullable
        @Override
        public MutableFieldData getField(String fieldName) {
            return fieldsMap.get(fieldName);
        }

        public MutableFieldData createField(String fieldName, String descriptor) {
            MutableFieldData field = makeField(fieldName, descriptor);
            fieldsMap.put(fieldName, field);
            return field;
        }

        public MutableFieldData getOrCreateField(String fieldName, String descriptor) {
            return fieldsMap.computeIfAbsent(fieldName, name -> this.makeField(name, descriptor));
        }

        private MutableFieldData makeField(String fieldName, String descriptor) {
            MutableFieldData field = new MutableFieldData(fieldName, descriptor);
            fields.add(field);
            return field;
        }

        public boolean removeField(String fieldName) {
            MutableFieldData data = fieldsMap.remove(fieldName);
            if (data == null) return false;
            fields.remove(data);
            return true;
        }

        public MutableClassData clearFields() {
            fields.clear();
            fieldsMap.clear();
            return this;
        }

        @Override
        public Collection<MutableMethodData> getMethods() {
            return methodsView;
        }

        @Nullable
        @Override
        public MutableMethodData getMethod(String methodName, String descriptor) {
            return methodsMap.get(key(methodName, descriptor));
        }

        public MutableMethodData createMethod(String methodName, String descriptor) {
            MutableMethodData method = makeMethod(methodName, descriptor);
            methodsMap.put(key(methodName, descriptor), method);
            return method;
        }

        public MutableMethodData getOrCreateMethod(String methodName, String descriptor) {
            return methodsMap.computeIfAbsent(key(methodName, descriptor), key -> this.makeMethod(methodName, descriptor));
        }

        private MutableMethodData makeMethod(String methodName, String descriptor) {
            MutableMethodData method = new MutableMethodData(methodName, descriptor);
            methods.add(method);
            return method;
        }

        public boolean removeMethod(String methodName, String descriptor) {
            MutableMethodData data = methodsMap.remove(key(methodName, descriptor));
            if (data == null) return false;
            methods.remove(data);
            return true;
        }

        public MutableClassData clearMethods() {
            methods.clear();
            methodsMap.clear();
            return this;
        }

        private String key(String methodName, String descriptor) {
            return methodName + ":" + descriptor;
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

    public static class MutableFieldData implements MappingDataContainer.FieldData, MutableHasJavadoc<MutableFieldData>, MutableHasConstants<MutableFieldData> {
        private final String name;
        private final String descriptor;
        private final List<String> javadoc = new ArrayList<>();
        private MutableConstantData constant;
        private transient final List<String> javadocView = Collections.unmodifiableList(javadoc);

        MutableFieldData(String name, String descriptor) {
            this.name = name;
            this.descriptor = descriptor;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescriptor() {
            return descriptor;
        }

        @Override
        public List<String> getJavadoc() {
            return javadocView;
        }

        @Override
        public MutableFieldData addJavadoc(Collection<? extends String> lines) {
            javadoc.addAll(lines);
            return this;
        }

        @Override
        public MutableFieldData clearJavadoc() {
            javadoc.clear();
            return this;
        }

        @Override
        public MutableConstantData getConstant() {
            return constant;
        }

        @Override
        public MutableFieldData setConstant(@Nullable ConstantData constantData) {
            if (constantData == null) {
                this.constant = null;
            } else {
                this.constant = new MutableConstantData(constantData);
            }
            return this;
        }

        @Override
        public MutableFieldData setConstantValues(List<ConstantValueData> values) {
            if (this.constant == null) {
                this.constant = new MutableConstantData();
            }

            this.constant.setConstantValues(values);
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MutableFieldData)) return false;

            MutableFieldData that = (MutableFieldData) o;

            if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
            if (getDescriptor() != null ? !getDescriptor().equals(that.getDescriptor()) : that.getDescriptor() != null)
                return false;
            if (getJavadoc() != null ? !getJavadoc().equals(that.getJavadoc()) : that.getJavadoc() != null)
                return false;
            if (getConstant() != null ? !getConstant().equals(that.getConstant()) : that.getConstant() != null)
                return false;
            return Objects.equals(javadocView, that.javadocView);
        }

        @Override
        public int hashCode() {
            int result = getName() != null ? getName().hashCode() : 0;
            result = 31 * result + (getDescriptor() != null ? getDescriptor().hashCode() : 0);
            result = 31 * result + (getJavadoc() != null ? getJavadoc().hashCode() : 0);
            result = 31 * result + (getConstant() != null ? getConstant().hashCode() : 0);
            result = 31 * result + (javadocView != null ? javadocView.hashCode() : 0);
            return result;
        }
    }

    public static class MutableMethodData implements MappingDataContainer.MethodData, MutableHasJavadoc<MutableMethodData> {
        private final String name;
        private final String descriptor;
        private final List<String> javadoc = new ArrayList<>();
        private transient final List<String> javadocView = Collections.unmodifiableList(javadoc);
        private final Set<MutableParameterData> parameters = new TreeSet<>(ParameterData.COMPARATOR);
        private transient final Map<Byte, MutableParameterData> parametersMap = new HashMap<>();
        private transient final Collection<MutableParameterData> parametersView = Collections.unmodifiableSet(parameters);

        MutableMethodData(String name, String descriptor) {
            this.name = name;
            this.descriptor = descriptor;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescriptor() {
            return descriptor;
        }

        @Override
        public List<String> getJavadoc() {
            return javadocView;
        }

        @Override
        public MutableMethodData addJavadoc(Collection<? extends String> lines) {
            javadoc.addAll(lines);
            return this;
        }

        @Override
        public MutableMethodData clearJavadoc() {
            javadoc.clear();
            return this;
        }

        @Override
        public Collection<MutableParameterData> getParameters() {
            return parametersView;
        }

        @Override
        @Nullable
        public MutableParameterData getParameter(byte index) {
            return parametersMap.get(index);
        }

        public MutableParameterData createParameter(byte index) {
            MutableParameterData param = makeParameter(index);
            parametersMap.put(index, param);
            return param;
        }

        public MutableParameterData getOrCreateParameter(byte index) {
            return parametersMap.computeIfAbsent(index, this::makeParameter);
        }

        private MutableParameterData makeParameter(byte index) {
            MutableParameterData param = new MutableParameterData(index);
            parameters.add(param);
            return param;
        }

        public boolean removeParameter(byte index) {
            MutableParameterData data = parametersMap.remove(index);
            if (data == null) return false;
            parameters.remove(data);
            return true;
        }

        public MutableMethodData clearParameters() {
            parameters.clear();
            parametersMap.clear();
            return this;
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

    public static class MutableParameterData implements MappingDataContainer.ParameterData, MutableHasJavadoc<MutableParameterData>, MutableHasConstants<MutableParameterData> {
        private final byte index;
        @Nullable
        private String name = null;
        @Nullable
        private String javadoc = null;
        private MutableConstantData constant = null;

        MutableParameterData(byte index) {
            this.index = index;
        }

        @Override
        public byte getIndex() {
            return index;
        }

        @Nullable
        @Override
        public String getName() {
            return name;
        }

        public MutableParameterData setName(@Nullable String name) {
            this.name = name;
            return this;
        }

        @Nullable
        @Override
        public String getJavadoc() {
            return javadoc;
        }

        public MutableParameterData setJavadoc(@Nullable String javadoc) {
            this.javadoc = javadoc;
            return this;
        }

        @Override
        public MutableParameterData addJavadoc(Collection<? extends String> lines) {
            this.javadoc = lines.stream().findFirst().orElse(null);
            return this;
        }

        @Override
        public MutableParameterData clearJavadoc() {
            this.javadoc = null;
            return this;
        }

        @Override
        public @Nullable MutableConstantData getConstant() {
            return constant;
        }

        @Override
        public MutableParameterData setConstant(@Nullable ConstantData constantData) {
            if (constantData == null) {
                this.constant = null;
            } else {
                this.constant = new MutableConstantData(constantData);
            }

            return this;
        }

        @Override
        public MutableParameterData setConstantValues(List<ConstantValueData> values) {
            if (this.constant == null)
                this.constant = new MutableConstantData();

            this.constant.setValues(values);

            return this;
        }
    }

    public static class MutableConstantData implements MappingDataContainer.ConstantData, MutableHasConstantValues<MutableConstantData> {

        private ConstantType type;
        private List<ConstantValueData> values = new ArrayList<>();

        public MutableConstantData(ConstantType type, List<ConstantValueData> values) {
            this.type = type;
            this.values = values;
        }

        public MutableConstantData(ConstantData from) {
            this(from.getType(), new ArrayList<>(from.getValues()));
        }

        public MutableConstantData() {
        }

        @Override
        public ConstantType getType() {
            return type;
        }

        @Override
        public List<ConstantValueData> getValues() {
            return values;
        }

        public MutableConstantData setValues(List<ConstantValueData> values) {
            this.values = values;
            return this;
        }

        public MutableConstantData setType(ConstantType type) {
            this.type = type;
            return this;
        }

        @Override
        public List<ConstantValueData> getConstantValues() {
            return values;
        }

        @Override
        public MutableConstantData setConstantValues(List<ConstantValueData> values) {
            this.values = values;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MutableConstantData)) return false;

            MutableConstantData that = (MutableConstantData) o;

            if (getType() != that.getType()) return false;
            return getValues() != null ? getValues().equals(that.getValues()) : that.getValues() == null;
        }

        @Override
        public int hashCode() {
            int result = getType() != null ? getType().hashCode() : 0;
            result = 31 * result + (getValues() != null ? getValues().hashCode() : 0);
            return result;
        }
    }

    public static class MutableConstantValueData implements MappingDataContainer.ConstantValueData {

        private int value;
        private String reference;

        public MutableConstantValueData(int value, String reference) {
            this.value = value;
            this.reference = reference;
        }

        public MutableConstantValueData(MappingDataContainer.ConstantValueData from) {
            this(from.getValue(), from.getReference());
        }

        public MutableConstantValueData() {
        }

        @Override
        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MutableConstantValueData)) return false;

            MutableConstantValueData that = (MutableConstantValueData) o;

            if (getValue() != that.getValue()) return false;
            return getReference() != null ? getReference().equals(that.getReference()) : that.getReference() == null;
        }

        @Override
        public int hashCode() {
            int result = getValue();
            result = 31 * result + (getReference() != null ? getReference().hashCode() : 0);
            return result;
        }
    }
}
