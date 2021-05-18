package org.parchmentmc.feather.io.gson.metadata;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.parchmentmc.feather.metadata.ImmutableMethodMetadata;
import org.parchmentmc.feather.metadata.MethodMetadata;
import org.parchmentmc.feather.metadata.MethodReference;
import org.parchmentmc.feather.named.Named;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * GSON adapter for {@link MethodMetadata} objects.
 *
 * <p>For internal use. Users should use {@link MetadataAdapterFactory} instead.</p>
 */
class MethodMetadataAdapter extends TypeAdapter<MethodMetadata> {
    private static final TypeToken<List<MethodReference>> METHOD_REFERENCE_LIST_TOKEN = new TypeToken<List<MethodReference>>() {
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
        out.name("bouncingTarget");
        gson.toJson(value.getBouncingTarget(), MethodReference.class, out);
        out.name("overrides");
        gson.toJson(value.getOverrides(), METHOD_REFERENCE_LIST_TOKEN.getType(), out);
        out.endObject();
    }

    @Override
    public MethodMetadata read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        Named name = null;
        Named owner = null;
        int security = -1;
        Named descriptor = null;
        Named signature = null;
        boolean lambda = false;
        MethodReference bouncingTarget = null;
        List<MethodReference> overrides = null;

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
                    bouncingTarget = gson.fromJson(in, MethodReference.class);
                    break;
                case "overrides":
                    overrides = gson.fromJson(in, METHOD_REFERENCE_LIST_TOKEN.getType());
                    break;
                default:
                    in.skipValue();
            }
        }

        if (name == null) throw new JsonParseException("Method metadata name is not present");
        if (owner == null) throw new JsonParseException("Method metadata owner is not present");
        if (descriptor == null) throw new JsonParseException("Method metadata descriptor is not present");
        if (signature == null) throw new JsonParseException("Method metadata signature is not present");
        if (security == -1) throw new JsonParseException("Method metadata security specification is not present");
        // lambda is a primitive
        // bouncingTarget can be null
        if (overrides == null) overrides = Collections.emptyList();

        return new ImmutableMethodMetadata(owner, lambda, bouncingTarget, overrides, name, security, descriptor, signature);
    }
}
