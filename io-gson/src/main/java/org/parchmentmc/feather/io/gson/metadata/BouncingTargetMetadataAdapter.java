package org.parchmentmc.feather.io.gson.metadata;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.parchmentmc.feather.metadata.BouncingTargetMetadata;
import org.parchmentmc.feather.metadata.BouncingTargetMetadataBuilder;
import org.parchmentmc.feather.metadata.Reference;

import java.io.IOException;

/**
 * GSON adapter for {@link BouncingTargetMetadata} objects.
 *
 * <p>For internal use. Users should use {@link MetadataAdapterFactory} instead.</p>
 */
class BouncingTargetMetadataAdapter extends TypeAdapter<BouncingTargetMetadata> {
    private final Gson gson;

    public BouncingTargetMetadataAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(final JsonWriter out, final BouncingTargetMetadata value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        if (value.getTarget().isPresent()) {
            out.name("target");
            gson.toJson(value.getTarget().get(), Reference.class, out);
        }

        if (value.getOwner().isPresent()) {
            out.name("owner");
            gson.toJson(value.getOwner().get(), Reference.class, out);
        }
        out.endObject();
    }

    @Override
    public BouncingTargetMetadata read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        final BouncingTargetMetadataBuilder builder = BouncingTargetMetadataBuilder.create();
        in.beginObject();
        while (in.hasNext()) {
            final String propertyName = in.nextName();
            switch (propertyName) {
                case "target":
                    builder.withTarget(
                            gson.fromJson(in, Reference.class)
                    );
                    break;
                case "owner":
                    builder.withOwner(
                            gson.fromJson(in, Reference.class)
                    );
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();

        return builder.build();
    }
}
