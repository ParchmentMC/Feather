package org.parchmentmc.feather.io.moshi;

import com.squareup.moshi.*;
import org.parchmentmc.feather.metadata.*;
import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.named.NamedBuilder;
import org.parchmentmc.feather.util.SimpleVersion;

import java.io.IOException;
import java.util.LinkedHashSet;
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
    void referenceToJson(JsonWriter writer,
                               Reference reference,
                               JsonAdapter<Named> namedAdapter) throws IOException {
        if (reference == null) {
            writer.nullValue();
            return;
        }

        writer.beginObject();

        writer.name("owner").jsonValue(namedAdapter.toJsonValue(reference.getOwner()));
        writer.name("name").jsonValue(namedAdapter.toJsonValue(reference.getName()));
        writer.name("descriptor").jsonValue(namedAdapter.toJsonValue(reference.getDescriptor()));
        writer.name("signature").jsonValue(namedAdapter.toJsonValue(reference.getSignature()));

        writer.endObject();
    }

    @ToJson
    void sourceToJson(JsonWriter writer,
                      SourceMetadata sourceMetadata,
                      JsonAdapter<SimpleVersion> specVersionAdapter,
                      JsonAdapter<LinkedHashSet<? extends ClassMetadata>> classLinkedHashSetAdapter) throws IOException {
        if (sourceMetadata == null) {
            writer.nullValue();
            return;
        }

        writer.beginObject();

        writer.name("specVersion").jsonValue(specVersionAdapter.toJsonValue(sourceMetadata.getSpecificationVersion()));
        writer.name("minecraftVersion").jsonValue(sourceMetadata.getMinecraftVersion());
        writer.name("classes").jsonValue(classLinkedHashSetAdapter.toJsonValue(sourceMetadata.getClasses()));

        writer.endObject();
    }

    @ToJson
    void classToJson(JsonWriter writer,
                     ClassMetadata classMetadata,
                     JsonAdapter<Named> namedAdapter,
                     JsonAdapter<LinkedHashSet<? extends Named>> namedLinkedHashSetAdapter,
                     JsonAdapter<LinkedHashSet<? extends MethodMetadata>> methodLinkedHashSetAdapter,
                     JsonAdapter<LinkedHashSet<? extends FieldMetadata>> fieldLinkedHashSetAdapter,
                     JsonAdapter<LinkedHashSet<? extends RecordMetadata>> recordLinkedHashSetAdapter,
                     JsonAdapter<LinkedHashSet<? extends ClassMetadata>> classLinkedHashSetAdapter) throws IOException {
        if (classMetadata == null) {
            writer.nullValue();
            return;
        }

        writer.beginObject();

        writer.name("name").jsonValue(namedAdapter.toJsonValue(classMetadata.getName()));
        writer.name("owner").jsonValue(namedAdapter.toJsonValue(classMetadata.getOwner()));
        writer.name("extends").jsonValue(namedAdapter.toJsonValue(classMetadata.getSuperName()));
        writer.name("security").jsonValue(classMetadata.getSecuritySpecification());
        writer.name("implements").jsonValue(namedLinkedHashSetAdapter.toJsonValue(classMetadata.getInterfaces()));
        writer.name("fields").jsonValue(fieldLinkedHashSetAdapter.toJsonValue(classMetadata.getFields()));
        writer.name("records").jsonValue(recordLinkedHashSetAdapter.toJsonValue(classMetadata.getRecords()));
        writer.name("methods").jsonValue(methodLinkedHashSetAdapter.toJsonValue(classMetadata.getMethods()));
        writer.name("inner").jsonValue(classLinkedHashSetAdapter.toJsonValue(classMetadata.getInnerClasses()));
        writer.name("signature").jsonValue(namedAdapter.toJsonValue(classMetadata.getSignature()));

        writer.endObject();
    }

    @ToJson
    void methodToJson(JsonWriter writer,
                      MethodMetadata methodMetadata,
                      JsonAdapter<Named> namedAdapter,
                      JsonAdapter<Reference> methodReferenceAdapter,
                      JsonAdapter<BouncingTargetMetadata> bouncingTargetMetadataAdapter,
                      JsonAdapter<LinkedHashSet<? extends Reference>> methodReferenceLinkedHashSetAdapter
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
        if (methodMetadata.getBouncingTarget().isPresent())
        {
            writer.name("bouncingTarget").jsonValue(bouncingTargetMetadataAdapter.toJsonValue(methodMetadata.getBouncingTarget().get()));
        }
        if (methodMetadata.getParent().isPresent())
        {
            writer.name("parent").jsonValue(methodReferenceAdapter.toJsonValue(methodMetadata.getParent().get()));
        }
        if (!methodMetadata.getOverrides().isEmpty())
        {
            writer.name("overrides").jsonValue(methodReferenceLinkedHashSetAdapter.toJsonValue(methodMetadata.getOverrides()));
        }

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

    @ToJson
    void recordToJson(JsonWriter writer,
      RecordMetadata fieldMetadata,
      JsonAdapter<Named> namedAdapter,
      JsonAdapter<Reference> referenceAdapter
    ) throws IOException {
        if (fieldMetadata == null) {
            writer.nullValue();
            return;
        }

        writer.beginObject();

        writer.name("owner").jsonValue(namedAdapter.toJsonValue(fieldMetadata.getOwner()));
        writer.name("field").jsonValue(referenceAdapter.toJsonValue(fieldMetadata.getField()));
        writer.name("getter").jsonValue(referenceAdapter.toJsonValue(fieldMetadata.getGetter()));

        writer.endObject();
    }

    @ToJson
    void bouncingTargetToJson(JsonWriter writer,
      BouncingTargetMetadata bouncingTargetMetadata,
      JsonAdapter<Reference> methodReferenceAdapter
    ) throws IOException {
        if (bouncingTargetMetadata == null) {
            writer.nullValue();
            return;
        }

        writer.beginObject();

        if (bouncingTargetMetadata.getTarget().isPresent())
            writer.name("target").jsonValue(methodReferenceAdapter.toJsonValue(bouncingTargetMetadata.getTarget().get()));
        if (bouncingTargetMetadata.getOwner().isPresent())
            writer.name("owner").jsonValue(methodReferenceAdapter.toJsonValue(bouncingTargetMetadata.getOwner().get()));

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
    Reference methodReferenceFromJson(JsonReader reader,
                                            JsonAdapter<Named> namedAdapter) throws IOException {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull();
        }

        reader.beginObject();

        final ReferenceBuilder builder = ReferenceBuilder.create();

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
                                  JsonAdapter<LinkedHashSet<? extends ClassMetadata>> classLinkedHashSetAdapter) throws IOException {
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
                    final LinkedHashSet<? extends ClassMetadata> classes = classLinkedHashSetAdapter.fromJson(reader);
                    builder.withClasses(classes == null ? new LinkedHashSet<>() : new LinkedHashSet<>(classes));
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
                                JsonAdapter<LinkedHashSet<? extends Named>> namedLinkedHashSetAdapter,
                                JsonAdapter<LinkedHashSet<? extends MethodMetadata>> methodLinkedHashSetAdapter,
                                JsonAdapter<LinkedHashSet<? extends FieldMetadata>> fieldLinkedHashSetAdapter,
                                JsonAdapter<LinkedHashSet<? extends ClassMetadata>> classLinkedHashSetAdapter) throws IOException {
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
                    final LinkedHashSet<? extends Named> interfaces = namedLinkedHashSetAdapter.fromJson(reader);
                    builder.withInterfaces(interfaces == null ? new LinkedHashSet<>() : new LinkedHashSet<>(interfaces));
                    break;
                case "fields":
                    final LinkedHashSet<? extends FieldMetadata> fields = fieldLinkedHashSetAdapter.fromJson(reader);
                    builder.withFields(fields == null ? new LinkedHashSet<>() : new LinkedHashSet<>(fields));
                    break;
                case "methods":
                    final LinkedHashSet<? extends MethodMetadata> methods = methodLinkedHashSetAdapter.fromJson(reader);
                    builder.withMethods(methods == null ? new LinkedHashSet<>() : new LinkedHashSet<>(methods));
                    break;
                case "inner":
                    final LinkedHashSet<? extends ClassMetadata> inner = classLinkedHashSetAdapter.fromJson(reader);
                    builder.withInnerClasses(inner == null ? new LinkedHashSet<>() : new LinkedHashSet<>(inner));
                    break;
                case "signature":
                    builder.withSignature(namedAdapter.fromJson(reader));
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
                                  JsonAdapter<Reference> methodReferenceAdapter,
                                  JsonAdapter<BouncingTargetMetadata> bouncingTargetMetadataAdapter,
                                  JsonAdapter<LinkedHashSet<? extends Reference>> methodReferenceLinkedHashSetAdapter) throws IOException {
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
                    builder.withBouncingTarget(bouncingTargetMetadataAdapter.fromJson(reader));
                    break;
                case "overrides":
                    final LinkedHashSet<? extends Reference> overrides = methodReferenceLinkedHashSetAdapter.fromJson(reader);
                    builder.withOverrides(overrides == null ? new LinkedHashSet<>() : new LinkedHashSet<>(overrides));
                    break;
                case "parent":
                    builder.withParent(methodReferenceAdapter.fromJson(reader));
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

    @FromJson
    RecordMetadata RecordFromJson(JsonReader reader,
      JsonAdapter<Named> namedAdapter,
      JsonAdapter<Reference> referenceAdapter) throws IOException {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull();
        }

        reader.beginObject();

        final RecordMetadataBuilder builder = RecordMetadataBuilder.create();

        while (reader.hasNext()) {
            final String paramName = reader.nextName();
            switch (paramName) {
                case "owner":
                    builder.withOwner(namedAdapter.fromJson(reader));
                    break;
                case "field":
                    builder.withField(referenceAdapter.fromJson(reader));
                    break;
                case "getter":
                    builder.withGetter(referenceAdapter.fromJson(reader));
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
    BouncingTargetMetadata BouncingMetadataFromJson(JsonReader reader,
      JsonAdapter<Reference> methodReferenceJsonAdapter) throws IOException {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull();
        }

        reader.beginObject();

        final BouncingTargetMetadataBuilder builder = BouncingTargetMetadataBuilder.create();

        while (reader.hasNext()) {
            final String paramName = reader.nextName();
            switch (paramName) {
                case "owner":
                    builder.withOwner(methodReferenceJsonAdapter.fromJson(reader));
                    break;
                case "target":
                    builder.withTarget(methodReferenceJsonAdapter.fromJson(reader));
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
