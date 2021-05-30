package org.parchmentmc.feather.io.gson.metadata;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.parchmentmc.feather.metadata.*;
import org.parchmentmc.feather.named.Named;

import java.io.IOException;
import java.util.LinkedHashSet;

/**
 * GSON adapter for {@link MethodMetadata} objects.
 *
 * <p>For internal use. Users should use {@link MetadataAdapterFactory} instead.</p>
 */
class MethodMetadataAdapter extends TypeAdapter<MethodMetadata> {
    private static final TypeToken<LinkedHashSet<MethodReference>> METHOD_REFERENCE_Set_TOKEN = new TypeToken<LinkedHashSet<MethodReference>>() {
    };
    private final Gson gson;

    public MethodMetadataAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, MethodMetadata value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        out.name("name");
        gson.toJson(value.getName(), Named.class, out);
        out.name("owner");
        gson.toJson(value.getOwner(), Named.class, out);
        out.name("security").value(value.getSecuritySpecification());
        out.name("descriptor");
        gson.toJson(value.getDescriptor(), Named.class, out);
        out.name("signature");
        gson.toJson(value.getSignature(), Named.class, out);
        out.name("lambda").value(value.isLambda());
        if (value.getBouncingTarget().isPresent()) {
            out.name("bouncingTarget");
            gson.toJson(value.getBouncingTarget().get(), BouncingTargetMetadata.class, out);
        }
        if (value.getParent().isPresent()) {
            out.name("parent");
            gson.toJson(value.getParent().get(), MethodReference.class, out);
        }
        if (!value.getOverrides().isEmpty()) {
            out.name("overrides");
            gson.toJson(value.getOverrides(), METHOD_REFERENCE_Set_TOKEN.getType(), out);
        }
        out.endObject();
    }

    @Override
    public MethodMetadata read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        Named name = Named.empty();
        Named owner = Named.empty();
        int security = -1;
        Named descriptor = Named.empty();
        Named signature = Named.empty();
        boolean lambda = false;
        BouncingTargetMetadata bouncingTarget = null;
        LinkedHashSet<MethodReference> overrides = null;
        int startLine = 0;
        int endLine = 0;
        MethodReference parent = null;

        in.beginObject();
        while (in.hasNext()) {
            final String propertyName = in.nextName();
            switch (propertyName) {
                case "name":
                    name = gson.fromJson(in, Named.class);
                    break;
                case "owner":
                    owner = gson.fromJson(in, Named.class);
                    break;
                case "security":
                    security = in.nextInt();
                    break;
                case "descriptor":
                    descriptor = gson.fromJson(in, Named.class);
                    break;
                case "signature":
                    signature = gson.fromJson(in, Named.class);
                    break;
                case "lambda":
                    lambda = in.nextBoolean();
                    break;
                case "bouncingTarget":
                    bouncingTarget = gson.fromJson(in, BouncingTargetMetadata.class);
                    break;
                case "parent":
                    parent = gson.fromJson(in, MethodReference.class);
                    break;
                case "overrides":
                    overrides = gson.fromJson(in, METHOD_REFERENCE_Set_TOKEN.getType());
                    break;
                case "startLine":
                    startLine = in.nextInt();
                    break;
                case "endLine":
                    endLine = in.nextInt();
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();

        if (name.isEmpty()) throw new JsonParseException("Method metadata name is not present or empty");
        if (owner.isEmpty()) throw new JsonParseException("Method metadata owner is not present or empty");
        if (descriptor.isEmpty()) throw new JsonParseException("Method metadata descriptor is not present or empty");
        if (signature.isEmpty()) throw new JsonParseException("Method metadata signature is not present or empty");
        if (security == -1) throw new JsonParseException("Method metadata security specification is not present");
        // lambda is a primitive
        // bouncingTarget can be null
        if (overrides == null) overrides = new LinkedHashSet<>();
        if (startLine < 0) throw new JsonParseException("Method metadata contains negative start line");
        if (endLine < 0) throw new JsonParseException("Method metadata contains negative end line");
        if (endLine < startLine) throw new JsonParseException("Method metadata contains end before start");

        return MethodMetadataBuilder.create()
          .withBouncingTarget(bouncingTarget)
          .withName(name)
          .withOwner(owner)
          .withDescriptor(descriptor)
          .withSignature(signature)
          .withSecuritySpecification(security)
          .withParent(parent)
          .withOverrides(overrides)
          .withStartLine(startLine)
          .withEndLine(endLine)
          .withLambda(lambda)
          .build();
    }
}
