package org.parchmentmc.feather.io.gson;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.parchmentmc.feather.mapping.ImmutableMappingDataContainer;
import org.parchmentmc.feather.mapping.ImmutableVersionedMappingDataContainer;
import org.parchmentmc.feather.mapping.MappingDataContainer;
import org.parchmentmc.feather.mapping.VersionedMappingDataContainer;
import org.parchmentmc.feather.util.SimpleVersion;

import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * GSON adapter factory for {@link VersionedMappingDataContainer}s and its inner data classes.
 */
public class MDCGsonAdapterFactory implements TypeAdapterFactory {
    private final boolean ignoreNonDocumented;

    public MDCGsonAdapterFactory(boolean ignoreNonDocumented) {
        this.ignoreNonDocumented = ignoreNonDocumented;
    }

    public MDCGsonAdapterFactory() {
        this(false);
    }

    /**
     * Returns whether this adapter will ignore mapping data entries which have no javadocs.
     *
     * @return if ignoring entries without javadocs
     */
    public boolean isIgnoreNonDocumented() {
        return ignoreNonDocumented;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Type type = typeToken.getType();
        if (type instanceof WildcardType) {
            WildcardType wildcard = (WildcardType) type;
            if (wildcard.getUpperBounds().length == 1) { // ? extends SomeType
                type = wildcard.getUpperBounds()[0];
            }
        }
        if (type.equals(VersionedMappingDataContainer.class)) {
            return (TypeAdapter<T>) new VersionedMappingDataContainerAdapter(gson).nullSafe();
        } else if (type.equals(MappingDataContainer.PackageData.class)) {
            return (TypeAdapter<T>) new PackageDataAdapter(gson, ignoreNonDocumented).nullSafe();
        } else if (type.equals(MappingDataContainer.ClassData.class)) {
            return (TypeAdapter<T>) new ClassDataAdapter(gson, ignoreNonDocumented).nullSafe();
        } else if (type.equals(MappingDataContainer.FieldData.class)) {
            return (TypeAdapter<T>) new FieldDataAdapter(gson, ignoreNonDocumented).nullSafe();
        } else if (type.equals(MappingDataContainer.MethodData.class)) {
            return (TypeAdapter<T>) new MethodDataAdapter(gson, ignoreNonDocumented).nullSafe();
        } else if (type.equals(MappingDataContainer.ParameterData.class)) {
            return (TypeAdapter<T>) new ParameterDataAdapter(gson, ignoreNonDocumented).nullSafe();
        } else if (type.equals(MappingDataContainer.ConstantData.class)) {
            return (TypeAdapter<T>) new ConstantDataAdapter(gson, ignoreNonDocumented).nullSafe();
        } else if (type.equals(MappingDataContainer.ConstantValueData.class)) {
            return (TypeAdapter<T>) new ConstantValueDataAdapter().nullSafe();
        }
        return null;
    }

    /**
     * GSON adapter for {@link MappingDataContainer}s.
     */
    static class VersionedMappingDataContainerAdapter extends TypeAdapter<VersionedMappingDataContainer> {
        static final TypeToken<Collection<? extends MappingDataContainer.PackageData>> PACKAGE_DATA_COLLECTION =
                new TypeToken<Collection<? extends MappingDataContainer.PackageData>>() {
                };
        static final TypeToken<Collection<? extends MappingDataContainer.ClassData>> CLASS_DATA_COLLECTION =
                new TypeToken<Collection<? extends MappingDataContainer.ClassData>>() {
                };
        private final Gson gson;

        VersionedMappingDataContainerAdapter(Gson gson) {
            this.gson = gson;
        }

        @Override
        public void write(JsonWriter writer, VersionedMappingDataContainer value) throws IOException {
            writer.beginObject();
            writer.name("version");
            gson.getAdapter(SimpleVersion.class).write(writer, value.getFormatVersion());
            writer.name("packages");
            gson.getAdapter(PACKAGE_DATA_COLLECTION).write(writer, value.getPackages());
            writer.name("classes");
            gson.getAdapter(CLASS_DATA_COLLECTION).write(writer, value.getClasses());
            writer.endObject();
        }

        @Override
        public VersionedMappingDataContainer read(JsonReader reader) throws IOException {
            SimpleVersion version = null;
            Collection<? extends MappingDataContainer.PackageData> packages = null;
            Collection<? extends MappingDataContainer.ClassData> classes = null;

            reader.beginObject();
            while (reader.hasNext()) {
                String propertyName = reader.nextName();
                switch (propertyName) {
                    case "version":
                        version = gson.getAdapter(SimpleVersion.class).read(reader);
                        if (version != null && !version.isCompatibleWith(VersionedMappingDataContainer.CURRENT_FORMAT))
                            throw new JsonParseException("Version " + version + " is incompatible with current version "
                                    + VersionedMappingDataContainer.CURRENT_FORMAT);
                        break;
                    case "packages":
                        packages = gson.getAdapter(PACKAGE_DATA_COLLECTION).read(reader);
                        break;
                    case "classes":
                        classes = gson.getAdapter(CLASS_DATA_COLLECTION).read(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();

            if (packages == null) packages = Collections.emptyList();
            if (classes == null) classes = Collections.emptyList();
            if (version == null) throw new JsonParseException("No version found");

            return new ImmutableVersionedMappingDataContainer(version, packages, classes);
        }
    }

    static final TypeToken<List<String>> STRING_LIST_TOKEN = new TypeToken<List<String>>() {
    };

    /**
     * GSON adapter for {@link MappingDataContainer.PackageData}s.
     */
    static class PackageDataAdapter extends TypeAdapter<MappingDataContainer.PackageData> {
        private final Gson gson;
        private final boolean ignoreNonDocumented;

        PackageDataAdapter(Gson gson, boolean ignoreNonDocumented) {
            this.gson = gson;
            this.ignoreNonDocumented = ignoreNonDocumented;
        }

        @Override
        public void write(JsonWriter writer, MappingDataContainer.PackageData packageData) throws IOException {
            if (ignoreNonDocumented && packageData.getJavadoc().isEmpty()) {
                writer.nullValue();
                return;
            }

            writer.beginObject()
                    .name("name").value(packageData.getName());
            if (!packageData.getJavadoc().isEmpty()) {
                writer.name("javadoc");
                gson.getAdapter(STRING_LIST_TOKEN).write(writer, packageData.getJavadoc());
            }
            writer.endObject();
        }

        @Override
        public MappingDataContainer.PackageData read(JsonReader reader) throws IOException {
            String name = null;
            List<String> javadoc = null;

            reader.beginObject();
            while (reader.hasNext()) {
                String propertyName = reader.nextName();
                switch (propertyName) {
                    case "name":
                        name = reader.nextString();
                        break;
                    case "javadoc":
                        javadoc = gson.getAdapter(STRING_LIST_TOKEN).read(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();

            if (name == null) throw new IllegalArgumentException("Package name must not be null");
            if (javadoc == null) javadoc = Collections.emptyList();

            return new ImmutableMappingDataContainer.ImmutablePackageData(name, javadoc);
        }
    }

    /**
     * GSON adapter for {@link MappingDataContainer.ClassData}s.
     */
    static class ClassDataAdapter extends TypeAdapter<MappingDataContainer.ClassData> {
        static final TypeToken<Collection<? extends MappingDataContainer.FieldData>> FIELD_DATA_COLLECTION =
                new TypeToken<Collection<? extends MappingDataContainer.FieldData>>() {
                };
        static final TypeToken<Collection<? extends MappingDataContainer.MethodData>> METHOD_DATA_COLLECTION =
                new TypeToken<Collection<? extends MappingDataContainer.MethodData>>() {
                };
        private final Gson gson;
        private final boolean ignoreNonDocumented;

        ClassDataAdapter(Gson gson, boolean ignoreNonDocumented) {
            this.gson = gson;
            this.ignoreNonDocumented = ignoreNonDocumented;
        }

        @Override
        public void write(JsonWriter writer, MappingDataContainer.ClassData classData) throws IOException {
            JsonElement fields = gson.getAdapter(FIELD_DATA_COLLECTION).toJsonTree(classData.getFields());
            JsonElement methods = gson.getAdapter(METHOD_DATA_COLLECTION).toJsonTree(classData.getMethods());
            if (ignoreNonDocumented && classData.getJavadoc().isEmpty()
                    && (fields == null || (fields instanceof JsonArray && fields.getAsJsonArray().size() == 0))
                    && (methods == null || (methods instanceof JsonArray && methods.getAsJsonArray().size() == 0))) {
                writer.nullValue();
                return;
            }

            writer.beginObject()
                    .name("name").value(classData.getName());
            if (!classData.getJavadoc().isEmpty()) {
                writer.name("javadoc");
                gson.getAdapter(STRING_LIST_TOKEN).write(writer, classData.getJavadoc());
            }
            if (!classData.getFields().isEmpty()) {
                writer.name("fields");
                gson.toJson(fields, writer);
            }
            if (!classData.getMethods().isEmpty()) {
                writer.name("methods");
                gson.toJson(methods, writer);
            }
            writer.endObject();
        }

        @Override
        public MappingDataContainer.ClassData read(JsonReader reader) throws IOException {
            String name = null;
            List<String> javadoc = null;
            Collection<? extends MappingDataContainer.FieldData> fields = null;
            Collection<? extends MappingDataContainer.MethodData> methods = null;

            reader.beginObject();
            while (reader.hasNext()) {
                String propertyName = reader.nextName();
                switch (propertyName) {
                    case "name":
                        name = reader.nextString();
                        break;
                    case "javadoc":
                        javadoc = gson.getAdapter(STRING_LIST_TOKEN).read(reader);
                        break;
                    case "fields":
                        fields = gson.getAdapter(FIELD_DATA_COLLECTION).read(reader);
                        break;
                    case "methods":
                        methods = gson.getAdapter(METHOD_DATA_COLLECTION).read(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();

            if (name == null) throw new IllegalArgumentException("Class name must not be null");
            if (javadoc == null) javadoc = Collections.emptyList();
            if (fields == null) fields = Collections.emptyList();
            if (methods == null) methods = Collections.emptyList();

            return new ImmutableMappingDataContainer.ImmutableClassData(name, javadoc, fields, methods);
        }
    }

    /**
     * GSON adapter for {@link MappingDataContainer.FieldData}s.
     */
    static class FieldDataAdapter extends TypeAdapter<MappingDataContainer.FieldData> {
        private final Gson gson;
        private final boolean ignoreNonDocumented;

        FieldDataAdapter(Gson gson, boolean ignoreNonDocumented) {
            this.gson = gson;
            this.ignoreNonDocumented = ignoreNonDocumented;
        }

        @Override
        public void write(JsonWriter writer, MappingDataContainer.FieldData fieldName) throws IOException {
            if (ignoreNonDocumented && fieldName.getJavadoc().isEmpty()) {
                writer.nullValue();
                return;
            }

            writer.beginObject()
                    .name("name").value(fieldName.getName())
                    .name("descriptor").value(fieldName.getDescriptor());
            if (!fieldName.getJavadoc().isEmpty()) {
                writer.name("javadoc");
                gson.getAdapter(STRING_LIST_TOKEN).write(writer, fieldName.getJavadoc());
            }
            if (fieldName.getConstant() != null) {
                writer.name("constant");
                gson.getAdapter(MappingDataContainer.ConstantData.class).write(writer, fieldName.getConstant());
            }
            writer.endObject();
        }

        @Override
        public MappingDataContainer.FieldData read(JsonReader reader) throws IOException {
            String name = null;
            String descriptor = null;
            List<String> javadoc = null;
            MappingDataContainer.ConstantData constant = null;

            reader.beginObject();
            while (reader.hasNext()) {
                String propertyName = reader.nextName();
                switch (propertyName) {
                    case "name":
                        name = reader.nextString();
                        break;
                    case "descriptor":
                        descriptor = reader.nextString();
                        break;
                    case "javadoc":
                        javadoc = gson.getAdapter(STRING_LIST_TOKEN).read(reader);
                        break;
                    case "constant":
                        constant = gson.getAdapter(MappingDataContainer.ConstantData.class).read(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();

            if (name == null) throw new IllegalArgumentException("Field name must not be null");
            if (descriptor == null) throw new IllegalArgumentException("Field descriptor must not be null");
            if (javadoc == null) javadoc = Collections.emptyList();

            return new ImmutableMappingDataContainer.ImmutableFieldData(name, descriptor, javadoc, constant);
        }
    }

    /**
     * GSON adapter for {@link MappingDataContainer.MethodData}s.
     */
    static class MethodDataAdapter extends TypeAdapter<MappingDataContainer.MethodData> {
        static final TypeToken<Collection<? extends MappingDataContainer.ParameterData>> PARAMETER_DATA_COLLECTION =
                new TypeToken<Collection<? extends MappingDataContainer.ParameterData>>() {
                };
        private final Gson gson;
        private final boolean ignoreNonDocumented;

        MethodDataAdapter(Gson gson, boolean ignoreNonDocumented) {
            this.gson = gson;
            this.ignoreNonDocumented = ignoreNonDocumented;
        }

        @Override
        public void write(JsonWriter writer, MappingDataContainer.MethodData methodData) throws IOException {
            JsonElement params = gson.getAdapter(PARAMETER_DATA_COLLECTION).toJsonTree(methodData.getParameters());
            if (ignoreNonDocumented && methodData.getJavadoc().isEmpty()
                    && (params == null || (params instanceof JsonArray && params.getAsJsonArray().size() == 0))) {
                writer.nullValue();
                return;
            }

            writer.beginObject()
                    .name("name").value(methodData.getName())
                    .name("descriptor").value(methodData.getDescriptor());
            if (!methodData.getJavadoc().isEmpty()) {
                writer.name("javadoc");
                gson.getAdapter(STRING_LIST_TOKEN).write(writer, methodData.getJavadoc());
            }
            if (!methodData.getParameters().isEmpty()) {
                writer.name("parameters");
                gson.toJson(params, writer);
            }
            writer.endObject();
        }

        @Override
        public MappingDataContainer.MethodData read(JsonReader reader) throws IOException {
            String name = null;
            String descriptor = null;
            List<String> javadoc = null;
            Collection<? extends MappingDataContainer.ParameterData> parameters = null;

            reader.beginObject();
            while (reader.hasNext()) {
                String propertyName = reader.nextName();
                switch (propertyName) {
                    case "name":
                        name = reader.nextString();
                        break;
                    case "descriptor":
                        descriptor = reader.nextString();
                        break;
                    case "javadoc":
                        javadoc = gson.getAdapter(STRING_LIST_TOKEN).read(reader);
                        break;
                    case "parameters":
                        parameters = gson.getAdapter(PARAMETER_DATA_COLLECTION).read(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();

            if (name == null) throw new IllegalArgumentException("Field name must not be null");
            if (descriptor == null) throw new IllegalArgumentException("Field descriptor must not be null");
            if (javadoc == null) javadoc = Collections.emptyList();
            if (parameters == null) parameters = Collections.emptyList();

            return new ImmutableMappingDataContainer.ImmutableMethodData(name, descriptor, javadoc, parameters);
        }
    }

    /**
     * GSON adapter for {@link MappingDataContainer.ParameterData}s.
     */
    static class ParameterDataAdapter extends TypeAdapter<MappingDataContainer.ParameterData> {
        private final Gson gson;
        private final boolean ignoreNonDocumented;

        ParameterDataAdapter(Gson gson, boolean ignoreNonDocumented) {
            this.gson = gson;
            this.ignoreNonDocumented = ignoreNonDocumented;
        }

        @Override
        public void write(JsonWriter writer, MappingDataContainer.ParameterData packageData) throws IOException {
            if (ignoreNonDocumented && packageData.getName() == null && packageData.getJavadoc() == null) {
                writer.nullValue();
                return;
            }

            writer.beginObject()
                    .name("index").value(packageData.getIndex());
            if (packageData.getName() != null) {
                writer.name("name").value(packageData.getName());
            }
            if (packageData.getJavadoc() != null) {
                writer.name("javadoc").value(packageData.getJavadoc());
            }
            if (packageData.getConstant() != null) {
                writer.name("constant");
                gson.getAdapter(MappingDataContainer.ConstantData.class).write(writer, packageData.getConstant());
            }
            writer.endObject();
        }

        @Override
        public MappingDataContainer.ParameterData read(JsonReader reader) throws IOException {

            byte index = -1;
            String name = null;
            String javadoc = null;
            MappingDataContainer.ConstantData constant = null;

            reader.beginObject();
            while (reader.hasNext()) {
                String propertyName = reader.nextName();
                switch (propertyName) {
                    case "index":
                        index = (byte) reader.nextInt();
                        break;
                    case "name":
                        name = reader.nextString();
                        break;
                    case "javadoc":
                        javadoc = reader.nextString();
                        break;
                    case "constant":
                        constant = gson.getAdapter(MappingDataContainer.ConstantData.class).read(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();

            if (index < 0) throw new IllegalArgumentException("Parameter index must be present and positive");

            return new ImmutableMappingDataContainer.ImmutableParameterData(index, name, javadoc, constant);
        }
    }

    static class ConstantDataAdapter extends TypeAdapter<MappingDataContainer.ConstantData> {
        static final TypeToken<Collection<? extends MappingDataContainer.ConstantValueData>> CONSTANT_VALUE_DATA_COLLECTION =
                new TypeToken<Collection<? extends MappingDataContainer.ConstantValueData>>() {
                };

        private final Gson gson;
        private final boolean ignoreNonDocumented;

        ConstantDataAdapter(Gson gson, boolean ignoreNonDocumented) {
            this.gson = gson;
            this.ignoreNonDocumented = ignoreNonDocumented;
        }

        @Override
        public void write(JsonWriter out, MappingDataContainer.ConstantData value) throws IOException {
            out.beginObject();

            out.name("type");
            gson.getAdapter(MappingDataContainer.ConstantType.class).write(out, value.getType());

            out.name("values");
            gson.getAdapter(CONSTANT_VALUE_DATA_COLLECTION).write(out, value.getValues());

            out.endObject();
        }

        @Override
        public MappingDataContainer.ConstantData read(JsonReader in) throws IOException {
            MappingDataContainer.ConstantType type = null;
            Collection<? extends MappingDataContainer.ConstantValueData> values = null;

            in.beginObject();
            while (in.hasNext()) {
                String propertyName = in.nextName();
                switch (propertyName) {
                    case "type":
                        type = gson.getAdapter(MappingDataContainer.ConstantType.class).read(in);
                        break;
                    case "values":
                        values = gson.getAdapter(CONSTANT_VALUE_DATA_COLLECTION).read(in);
                        break;
                    default:
                        in.skipValue();
                        break;
                }
            }
            in.endObject();

            if (type == null) throw new IllegalArgumentException("Field type must not be null");
            if (values == null) values = Collections.emptyList();

            return new ImmutableMappingDataContainer.ImmutableConstantData(type, new ArrayList<>(values));
        }
    }

    static class ConstantValueDataAdapter extends TypeAdapter<MappingDataContainer.ConstantValueData> {

        @Override
        public void write(JsonWriter out, MappingDataContainer.ConstantValueData value) throws IOException {
            out.beginObject();

            out.name("value").value(value.getValue());
            out.name("reference").value(value.getReference());

            out.endObject();
        }

        @Override
        public MappingDataContainer.ConstantValueData read(JsonReader in) throws IOException {
            Integer value = null;
            String reference = null;

            in.beginObject();
            while (in.hasNext()) {
                String propertyName = in.nextName();
                switch (propertyName) {
                    case "value":
                        value = in.nextInt();
                        break;
                    case "reference":
                        reference = in.nextString();
                        break;
                    default:
                        in.skipValue();
                        break;
                }
            }
            in.endObject();

            if (value == null) throw new IllegalArgumentException("Field value must not be null");
            if (reference == null) throw new IllegalArgumentException("Field reference must not be null");

            return new ImmutableMappingDataContainer.ImmutableConstantValueData(value, reference);
        }
    }
}
