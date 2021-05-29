package org.parchmentmc.feather.io.moshi;

import com.squareup.moshi.*;
import org.parchmentmc.feather.mapping.ImmutableMappingDataContainer;
import org.parchmentmc.feather.mapping.ImmutableVersionedMappingDataContainer;
import org.parchmentmc.feather.mapping.MappingDataContainer;
import org.parchmentmc.feather.mapping.VersionedMappingDataContainer;
import org.parchmentmc.feather.util.SimpleVersion;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Moshi adapter for {@link VersionedMappingDataContainer}s and its inner data classes.
 */
@SuppressWarnings("unused")
public class MDCMoshiAdapter {
    private final boolean ignoreNonDocumented;

    public MDCMoshiAdapter(boolean ignoreNonDocumented) {
        this.ignoreNonDocumented = ignoreNonDocumented;
    }

    public MDCMoshiAdapter() {
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

    /* ****************** Serialization ****************** */

    @ToJson
    void containerToJson(JsonWriter writer,
                         VersionedMappingDataContainer container,
                         JsonAdapter<SimpleVersion> versionAdapter,
                         JsonAdapter<Collection<? extends MappingDataContainer.PackageData>> packageAdapter,
                         JsonAdapter<Collection<? extends MappingDataContainer.ClassData>> classAdapter) throws IOException {
        writer.beginObject();
        writer.name("version").jsonValue(versionAdapter.toJsonValue(container.getFormatVersion()));
        writer.name("packages").jsonValue(packageAdapter.toJsonValue(container.getPackages()));
        writer.name("classes").jsonValue(classAdapter.toJsonValue(container.getClasses()));
        writer.endObject();
    }

    @ToJson
    void packageToJson(JsonWriter writer,
                       MappingDataContainer.PackageData packageData,
                       JsonAdapter<List<String>> stringListAdapter) throws IOException {
        if (isIgnoreNonDocumented() && packageData.getJavadoc().isEmpty()) return;

        writer.beginObject()
                .name("name").value(packageData.getName());
        if (!packageData.getJavadoc().isEmpty())
            writer.name("javadoc").jsonValue(stringListAdapter.toJsonValue(packageData.getJavadoc()));
        writer.endObject();
    }

    @ToJson
    void classToJson(JsonWriter writer,
                     MappingDataContainer.ClassData classData,
                     JsonAdapter<List<String>> stringListAdapter,
                     JsonAdapter<Collection<? extends MappingDataContainer.FieldData>> fieldAdapter,
                     JsonAdapter<Collection<? extends MappingDataContainer.MethodData>> methodAdapter) throws IOException {
        Object fields = fieldAdapter.toJsonValue(classData.getFields());
        Object methods = methodAdapter.toJsonValue(classData.getMethods());
        if (isIgnoreNonDocumented()
                && (fields == null || (fields instanceof Collection && ((Collection<?>) fields).isEmpty()))
                && (methods == null || (methods instanceof Collection && ((Collection<?>) methods).isEmpty()))
                && classData.getJavadoc().isEmpty()) return;

        writer.beginObject()
                .name("name").value(classData.getName());
        if (!classData.getJavadoc().isEmpty())
            writer.name("javadoc").jsonValue(stringListAdapter.toJsonValue(classData.getJavadoc()));
        if (!classData.getFields().isEmpty())
            writer.name("fields").jsonValue(fields);
        if (!classData.getMethods().isEmpty())
            writer.name("methods").jsonValue(methods);
        writer.endObject();
    }

    @ToJson
    void fieldToJson(JsonWriter writer,
                     MappingDataContainer.FieldData fieldData,
                     JsonAdapter<List<String>> stringListAdapter) throws IOException {
        if (isIgnoreNonDocumented() && fieldData.getJavadoc().isEmpty()) return;

        writer.beginObject()
                .name("name").value(fieldData.getName())
                .name("descriptor").value(fieldData.getDescriptor());
        if (!fieldData.getJavadoc().isEmpty())
            writer.name("javadoc").jsonValue(stringListAdapter.toJsonValue(fieldData.getJavadoc()));
        writer.endObject();
    }

    @ToJson
    void methodToJson(JsonWriter writer,
                      MappingDataContainer.MethodData methodData,
                      JsonAdapter<List<String>> stringListAdapter,
                      JsonAdapter<Collection<? extends MappingDataContainer.ParameterData>> paramAdapter) throws IOException {
        Object params = paramAdapter.toJsonValue(methodData.getParameters());
        if (isIgnoreNonDocumented()
                && (params == null || (params instanceof Collection && ((Collection<?>) params).isEmpty()))
                && methodData.getJavadoc().isEmpty()) return;

        writer.beginObject()
                .name("name").value(methodData.getName())
                .name("descriptor").value(methodData.getDescriptor());
        if (!methodData.getJavadoc().isEmpty())
            writer.name("javadoc").jsonValue(stringListAdapter.toJsonValue(methodData.getJavadoc()));
        if (!methodData.getParameters().isEmpty())
            writer.name("parameters").jsonValue(params);
        writer.endObject();
    }

    @ToJson
    void paramToJson(JsonWriter writer,
                     MappingDataContainer.ParameterData paramData) throws IOException {
        if (isIgnoreNonDocumented() && paramData.getName() == null && paramData.getJavadoc() == null) return;

        writer.beginObject()
                .name("index").value(paramData.getIndex());
        if (paramData.getName() != null)
            writer.name("name").value(paramData.getName());
        if (paramData.getJavadoc() != null)
            writer.name("javadoc").value(paramData.getJavadoc());
        writer.endObject();
    }

    /* ***************** Deserialization ***************** */

    @FromJson
    VersionedMappingDataContainer containerToJson(JsonReader reader,
                                                  JsonAdapter<SimpleVersion> versionAdapter,
                                                  JsonAdapter<Collection<? extends MappingDataContainer.PackageData>> packageAdapter,
                                                  JsonAdapter<Collection<? extends MappingDataContainer.ClassData>> classAdapter) throws IOException {

        SimpleVersion version = null;
        Collection<? extends MappingDataContainer.PackageData> packages = null;
        Collection<? extends MappingDataContainer.ClassData> classes = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String propertyName = reader.nextName();
            switch (propertyName) {
                case "version":
                    version = versionAdapter.fromJson(reader);
                    if (version != null && !version.isCompatibleWith(VersionedMappingDataContainer.CURRENT_FORMAT))
                        throw new JsonDataException("Version " + version + " is incompatible with current version "
                                + VersionedMappingDataContainer.CURRENT_FORMAT);
                    break;
                case "packages":
                    packages = packageAdapter.fromJson(reader);
                    break;
                case "classes":
                    classes = classAdapter.fromJson(reader);
                    break;
                default:
                    reader.skipName();
                    break;
            }
        }
        reader.endObject();

        if (packages == null) packages = Collections.emptyList();
        if (classes == null) classes = Collections.emptyList();
        if (version == null) throw new JsonDataException("No version found");

        return new ImmutableVersionedMappingDataContainer(version, packages, classes);
    }

    @FromJson
    MappingDataContainer.PackageData packageFromJson(JsonReader reader,
                                                     JsonAdapter<List<String>> stringListAdapter) throws IOException {

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
                    javadoc = stringListAdapter.fromJson(reader);
                    break;
                default:
                    reader.skipName();
                    break;
            }
        }
        reader.endObject();

        if (name == null) throw new IllegalArgumentException("Package name must not be null");
        if (javadoc == null) javadoc = Collections.emptyList();

        return new ImmutableMappingDataContainer.ImmutablePackageData(name, javadoc);
    }


    @FromJson
    MappingDataContainer.ClassData classFromJson(JsonReader reader,
                                                 JsonAdapter<List<String>> stringListAdapter,
                                                 JsonAdapter<Collection<? extends MappingDataContainer.FieldData>> fieldAdapter,
                                                 JsonAdapter<Collection<? extends MappingDataContainer.MethodData>> methodAdapter) throws IOException {
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
                    javadoc = stringListAdapter.fromJson(reader);
                    break;
                case "fields":
                    fields = fieldAdapter.fromJson(reader);
                    break;
                case "methods":
                    methods = methodAdapter.fromJson(reader);
                    break;
                default:
                    reader.skipName();
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

    @FromJson
    MappingDataContainer.FieldData fieldFromJson(JsonReader reader,
                                                 JsonAdapter<List<String>> stringListAdapter) throws IOException {
        String name = null;
        String descriptor = null;
        List<String> javadoc = null;

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
                    javadoc = stringListAdapter.fromJson(reader);
                    break;
                default:
                    reader.skipName();
                    break;
            }
        }
        reader.endObject();

        if (name == null) throw new IllegalArgumentException("Field name must not be null");
        if (descriptor == null) throw new IllegalArgumentException("Field descriptor must not be null");
        if (javadoc == null) javadoc = Collections.emptyList();

        return new ImmutableMappingDataContainer.ImmutableFieldData(name, descriptor, javadoc);
    }

    @FromJson
    MappingDataContainer.MethodData methodFromJson(JsonReader reader,
                                                   JsonAdapter<List<String>> stringListAdapter,
                                                   JsonAdapter<Collection<? extends MappingDataContainer.ParameterData>> paramAdapter) throws IOException {
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
                    javadoc = stringListAdapter.fromJson(reader);
                    break;
                case "parameters":
                    parameters = paramAdapter.fromJson(reader);
                    break;
                default:
                    reader.skipName();
                    break;
            }
        }
        reader.endObject();

        if (name == null) throw new IllegalArgumentException("Method name must not be null");
        if (descriptor == null) throw new IllegalArgumentException("Method descriptor must not be null");
        if (javadoc == null) javadoc = Collections.emptyList();
        if (parameters == null) parameters = Collections.emptyList();

        return new ImmutableMappingDataContainer.ImmutableMethodData(name, descriptor, javadoc, parameters);
    }

    @FromJson
    MappingDataContainer.ParameterData paramToJson(JsonReader reader) throws IOException {

        byte index = -1;
        String name = null;
        String javadoc = null;

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
                default:
                    reader.skipName();
                    break;
            }
        }
        reader.endObject();

        if (index < 0) throw new IllegalArgumentException("Parameter index must be present and positive");

        return new ImmutableMappingDataContainer.ImmutableParameterData(index, name, javadoc);
    }
}
