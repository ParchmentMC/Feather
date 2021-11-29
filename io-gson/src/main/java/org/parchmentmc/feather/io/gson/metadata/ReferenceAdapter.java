package org.parchmentmc.feather.io.gson.metadata;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.parchmentmc.feather.metadata.Reference;
import org.parchmentmc.feather.metadata.ReferenceBuilder;
import org.parchmentmc.feather.named.Named;

import java.io.IOException;

/**
 * GSON adapter for {@link Reference} objects.
 *
 * <p>For internal use. Users should use {@link MetadataAdapterFactory} instead.</p>
 */
class ReferenceAdapter extends TypeAdapter<Reference> {
    private final Gson gson;

    public ReferenceAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, Reference value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        out.name("name");
        gson.toJson(value.getName(), Named.class, out);
        out.name("owner");
        gson.toJson(value.getOwner(), Named.class, out);
        out.name("descriptor");
        gson.toJson(value.getDescriptor(), Named.class, out);
        out.name("signature");
        gson.toJson(value.getSignature(), Named.class, out);
        out.endObject();
    }

    @Override
    public Reference read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        Named name = Named.empty();
        Named owner = Named.empty();
        Named descriptor = Named.empty();
        Named signature = Named.empty();

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
                case "descriptor":
                    descriptor = gson.fromJson(in, Named.class);
                    break;
                case "signature":
                    signature = gson.fromJson(in, Named.class);
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();

        if (name.isEmpty()) throw new JsonParseException("Method reference name is not present");
        if (owner.isEmpty()) throw new JsonParseException("Method reference owner is not present");
        if (descriptor.isEmpty()) throw new JsonParseException("Method reference descriptor is not present");

        return ReferenceBuilder.create()
          .withOwner(owner)
          .withName(name)
          .withSignature(signature)
          .withDescriptor(descriptor)
          .build();
    }
}
