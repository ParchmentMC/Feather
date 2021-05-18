package org.parchmentmc.feather.io.gson.metadata;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.parchmentmc.feather.metadata.FieldMetadata;
import org.parchmentmc.feather.metadata.ImmutableFieldMetadata;
import org.parchmentmc.feather.named.Named;

import java.io.IOException;

/**
 * GSON adapter for {@link FieldMetadata} objects.
 *
 * <p>For internal use. Users should use {@link MetadataAdapterFactory} instead.</p>
 */
class FieldMetadataAdapter extends TypeAdapter<FieldMetadata> {
    private final Gson gson;

    public FieldMetadataAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, FieldMetadata value) throws IOException {
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
        out.endObject();
    }

    @Override
    public FieldMetadata read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        Named name = null;
        Named owner = null;
        int security = -1;
        Named descriptor = null;
        Named signature = null;

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
                default:
                    in.skipValue();
            }
        }

        if (name == null) throw new JsonParseException("Field name is not present");
        if (owner == null) throw new JsonParseException("Field owner is not present");
        if (security == -1) throw new JsonParseException("Field security specification is not present");
        if (descriptor == null) throw new JsonParseException("Field descriptor is not present");
        if (signature == null) throw new JsonParseException("Field signature is not present");

        return new ImmutableFieldMetadata(name, owner, security, descriptor, signature);
    }
}
