package org.parchmentmc.feather.io.moshi;

import com.squareup.moshi.*;
import org.parchmentmc.feather.metadata.*;
import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.named.NamedBuilder;
import org.parchmentmc.feather.util.SimpleVersion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@SuppressWarnings("unused")
public class MetadataMoshiAdapter {

    /* ****************** Serialization ****************** */

    @ToJson
    void namedToJson(JsonWriter writer,
                     Named named) throws IOException {
        if (named == null) {
            writer.nullValue();
            return;
        }

        writer.beginObject();

        for (Map.Entry<String, String> entry : named.getNames().entrySet()) {
            String schema = entry.getKey();
            String name = entry.getValue();
            writer.name(schema).jsonValue(name);
        }

        writer.endObject();
    }

    @ToJson
    void methodReferenceToJson(JsonWriter writer,
                               MethodReference methodReference,
                               JsonAdapter<Named> namedAdapter) throws IOException {
        if (methodReference == null) {
            writer.nullValue();
            return;
        }

        writer.beginObject();

        writer.name("owner").jsonValue(namedAdapter.toJsonValue(methodReference.getOwner()));
        writer.name("name").jsonValue(namedAdapter.toJsonValue(methodReference.getName()));
        writer.name("descriptor").jsonValue(namedAdapter.toJsonValue(methodReference.getDescriptor()));
        writer.name("signature").jsonValue(namedAdapter.toJsonValue(methodReference.getSignature()));

        writer.endObject();
    }

    @ToJson
    void sourceToJson(JsonWriter writer,
                      SourceMetadata sourceMetadata,
                      JsonAdapter<SimpleVersion> specVersionAdapter,
                      JsonAdapter<Collection<? extends ClassMetadata>> classCollectionAdapter) throws IOException {
        if (sourceMetadata == null) {
            writer.nullValue();
            return;
        }

        writer.beginObject();

        writer.name("specVersion").jsonValue(specVersionAdapter.toJsonValue(sourceMetadata.getSpecificationVersion()));
        writer.name("minecraftVersion").jsonValue(sourceMetadata.getMinecraftVersion());
        writer.name("classes").jsonValue(classCollectionAdapter.toJsonValue(sourceMetadata.getClasses()));

        writer.endObject();
    }

    @ToJson
    void classToJson(JsonWriter writer,
                     ClassMetadata classMetadata,
                     JsonAdapter<Named> namedAdapter,
                     JsonAdapter<Collection<? extends Named>> namedCollectionAdapter,
                     JsonAdapter<Collection<? extends MethodMetadata>> methodCollectionAdapter,
                     JsonAdapter<Collection<? extends FieldMetadata>> fieldCollectionAdapter,
                     JsonAdapter<Collection<? extends ClassMetadata>> classCollectionAdapter) throws IOException {
        if (classMetadata == null) {
            writer.nullValue();
            return;
        }

        writer.beginObject();

        writer.name("name").jsonValue(namedAdapter.toJsonValue(classMetadata.getName()));
        writer.name("owner").jsonValue(namedAdapter.toJsonValue(classMetadata.getOwner()));
        writer.name("extends").jsonValue(namedAdapter.toJsonValue(classMetadata.getSuperName()));
        writer.name("security").jsonValue(classMetadata.getSecuritySpecification());
        writer.name("implements").jsonValue(namedCollectionAdapter.toJsonValue(classMetadata.getInterfaces()));
        writer.name("fields").jsonValue(fieldCollectionAdapter.toJsonValue(classMetadata.getFields()));
        writer.name("methods").jsonValue(methodCollectionAdapter.toJsonValue(classMetadata.getMethods()));
        writer.name("inner").jsonValue(classCollectionAdapter.toJsonValue(classMetadata.getInnerClasses()));

        writer.endObject();
    }

    @ToJson
    void methodToJson(JsonWriter writer,
                      MethodMetadata methodMetadata,
                      JsonAdapter<Named> namedAdapter,
                      JsonAdapter<MethodReference> methodReferenceAdapter,
                      JsonAdapter<Collection<? extends MethodReference>> methodReferenceCollectionAdapter
    ) throws IOException {
        if (methodMetadata == null) {
            writer.nullValue();
            return;
        }

        writer.beginObject();

        writer.name("owner").jsonValue(namedAdapter.toJsonValue(methodMetadata.getOwner()));
        writer.name("name").jsonValue(namedAdapter.toJsonValue(methodMetadata.getName()));
        writer.name("descriptor").jsonValue(namedAdapter.toJsonValue(methodMetadata.getDescriptor()));
        writer.name("signature").jsonValue(namedAdapter.toJsonValue(methodMetadata.getSignature()));
        writer.name("security").jsonValue(methodMetadata.getSecuritySpecification());
        writer.name("lambda").jsonValue(methodMetadata.isLambda());
        writer.name("bouncingTarget").jsonValue(methodReferenceAdapter.toJsonValue(methodMetadata.getBouncingTarget()));
        writer.name("overrides").jsonValue(methodReferenceCollectionAdapter.toJsonValue(methodMetadata.getOverrides()));

        writer.endObject();
    }

    @ToJson
    void fieldToJson(JsonWriter writer,
                     FieldMetadata fieldMetadata,
                     JsonAdapter<Named> namedAdapter
    ) throws IOException {
        if (fieldMetadata == null) {
            writer.nullValue();
            return;
        }

        writer.beginObject();

        writer.name("owner").jsonValue(namedAdapter.toJsonValue(fieldMetadata.getOwner()));
        writer.name("name").jsonValue(namedAdapter.toJsonValue(fieldMetadata.getName()));
        writer.name("descriptor").jsonValue(namedAdapter.toJsonValue(fieldMetadata.getDescriptor()));
        writer.name("signature").jsonValue(namedAdapter.toJsonValue(fieldMetadata.getSignature()));
        writer.name("security").jsonValue(fieldMetadata.getSecuritySpecification());

        writer.endObject();
    }

    /* ***************** Deserialization ***************** */

    @FromJson
    Named namedFromJson(JsonReader reader) throws IOException {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull();
        }

        final NamedBuilder builder = NamedBuilder.create();

        reader.beginObject();
        while (reader.hasNext()) {
            final String schema = reader.nextName();
            final String name = reader.nextString();

            builder.with(schema, name);
        }
        reader.endObject();

        return builder.build();
    }

    @FromJson
    MethodReference methodReferenceFromJson(JsonReader reader,
                                            JsonAdapter<Named> namedAdapter) throws IOException {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull();
        }

        reader.beginObject();

        final MethodReferenceBuilder builder = MethodReferenceBuilder.create();

        while (reader.hasNext()) {
            final String paramName = reader.nextName();
            switch (paramName) {
                case "owner":
                    builder.withOwner(namedAdapter.fromJson(reader));
                    break;
                case "name":
                    builder.withName(namedAdapter.fromJson(reader));
                    break;
                case "descriptor":
                    builder.withDescriptor(namedAdapter.fromJson(reader));
                    break;
                case "signature":
                    builder.withSignature(namedAdapter.fromJson(reader));
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();

        return builder.build();
    }

    @FromJson
    SourceMetadata sourceFromJson(JsonReader reader,
                                  JsonAdapter<SimpleVersion> specVersionAdapter,
                                  JsonAdapter<Collection<? extends ClassMetadata>> classCollectionAdapter) throws IOException {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull();
        }

        reader.beginObject();

        final SourceMetadataBuilder builder = SourceMetadataBuilder.create();

        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "specVersion":
                    builder.withSpecVersion(specVersionAdapter.fromJson(reader));
                    break;
                case "minecraftVersion":
                    builder.withMinecraftVersion(reader.nextString());
                    break;
                case "classes":
                    final Collection<? extends ClassMetadata> classes = classCollectionAdapter.fromJson(reader);
                    builder.withClasses(classes == null ? Collections.emptyList() : new ArrayList<>(classes));
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();

        return builder.build();
    }

    @FromJson
    ClassMetadata classFromJson(JsonReader reader,
                                JsonAdapter<Named> namedAdapter,
                                JsonAdapter<Collection<? extends Named>> namedCollectionAdapter,
                                JsonAdapter<Collection<? extends MethodMetadata>> methodCollectionAdapter,
                                JsonAdapter<Collection<? extends FieldMetadata>> fieldCollectionAdapter,
                                JsonAdapter<Collection<? extends ClassMetadata>> classCollectionAdapter) throws IOException {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull();
        }

        final ClassMetadataBuilder builder = ClassMetadataBuilder.create();

        reader.beginObject();

        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "name":
                    builder.withName(namedAdapter.fromJson(reader));
                    break;
                case "owner":
                    builder.withOwner(namedAdapter.fromJson(reader));
                    break;
                case "extends":
                    builder.withSuperName(namedAdapter.fromJson(reader));
                    break;
                case "security":
                    builder.withSecuritySpecifications(reader.nextInt());
                    break;
                case "implements":
                    final Collection<? extends Named> interfaces = namedCollectionAdapter.fromJson(reader);
                    builder.withInterfaces(interfaces == null ? Collections.emptyList() : new ArrayList<>(interfaces));
                    break;
                case "fields":
                    final Collection<? extends FieldMetadata> fields = fieldCollectionAdapter.fromJson(reader);
                    builder.withFields(fields == null ? Collections.emptyList() : new ArrayList<>(fields));
                    break;
                case "methods":
                    final Collection<? extends MethodMetadata> methods = methodCollectionAdapter.fromJson(reader);
                    builder.withMethods(methods == null ? Collections.emptyList() : new ArrayList<>(methods));
                    break;
                case "inner":
                    final Collection<? extends ClassMetadata> inner = classCollectionAdapter.fromJson(reader);
                    builder.withInnerClasses(inner == null ? Collections.emptyList() : new ArrayList<>(inner));
                    break;
                default:
                    reader.skipValue();
            }
        }

        reader.endObject();

        return builder.build();
    }

    @FromJson
    MethodMetadata methodFromJson(JsonReader reader,
                                  JsonAdapter<Named> namedAdapter,
                                  JsonAdapter<MethodReference> methodReferenceAdapter,
                                  JsonAdapter<Collection<? extends MethodReference>> methodReferenceCollectionAdapter) throws IOException {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull();
        }

        reader.beginObject();

        final MethodMetadataBuilder builder = MethodMetadataBuilder.create();

        while (reader.hasNext()) {
            final String paramName = reader.nextName();
            switch (paramName) {
                case "owner":
                    builder.withOwner(namedAdapter.fromJson(reader));
                    break;
                case "name":
                    builder.withName(namedAdapter.fromJson(reader));
                    break;
                case "descriptor":
                    builder.withDescriptor(namedAdapter.fromJson(reader));
                    break;
                case "signature":
                    builder.withSignature(namedAdapter.fromJson(reader));
                    break;
                case "security":
                    builder.withSecuritySpecification(reader.nextInt());
                    break;
                case "lambda":
                    builder.withLambda(reader.nextBoolean());
                    break;
                case "bouncingTarget":
                    builder.withBouncingTarget(methodReferenceAdapter.fromJson(reader));
                    break;
                case "overrides":
                    final Collection<? extends MethodReference> overrides = methodReferenceCollectionAdapter.fromJson(reader);
                    builder.withOverrides(overrides == null ? Collections.emptyList() : new ArrayList<>(overrides));
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();

        return builder.build();
    }

    @FromJson
    FieldMetadata FieldFromJson(JsonReader reader,
                                JsonAdapter<Named> namedAdapter) throws IOException {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull();
        }

        reader.beginObject();

        final FieldMetadataBuilder builder = FieldMetadataBuilder.create();

        while (reader.hasNext()) {
            final String paramName = reader.nextName();
            switch (paramName) {
                case "owner":
                    builder.withOwner(namedAdapter.fromJson(reader));
                    break;
                case "name":
                    builder.withName(namedAdapter.fromJson(reader));
                    break;
                case "descriptor":
                    builder.withDescriptor(namedAdapter.fromJson(reader));
                    break;
                case "signature":
                    builder.withSignature(namedAdapter.fromJson(reader));
                    break;
                case "security":
                    builder.withSecuritySpecification(reader.nextInt());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();

        return builder.build();
    }
}
