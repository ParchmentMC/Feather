package org.parchmentmc.feather.io.gson.metadata;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.parchmentmc.feather.metadata.RecordMetadata;
import org.parchmentmc.feather.metadata.RecordMetadataBuilder;
import org.parchmentmc.feather.metadata.Reference;
import org.parchmentmc.feather.named.Named;

import java.io.IOException;

/**
 * GSON adapter for {@link RecordMetadata} objects.
 *
 * <p>For internal use. Users should use {@link MetadataAdapterFactory} instead.</p>
 */
class RecordMetadataAdapter extends TypeAdapter<RecordMetadata>
{
    private final Gson gson;

    public RecordMetadataAdapter(Gson gson)
    {
        this.gson = gson;
    }

    @Override
    public void write(final JsonWriter out, final RecordMetadata value) throws IOException
    {
        out.beginObject();
        out.name("owner");
        gson.toJson(value.getOwner(), Named.class, out);
        out.name("field");
        gson.toJson(value.getField(), Reference.class, out);
        out.name("getter");
        gson.toJson(value.getGetter(), Reference.class, out);
        out.endObject();
    }

    @Override
    public RecordMetadata read(final JsonReader in) throws IOException
    {
        if (in.peek() == JsonToken.NULL)
        {
            in.nextNull();
            return null;
        }

        Named owner = Named.empty();
        Reference field = null;
        Reference getter = null;

        in.beginObject();
        while (in.hasNext())
        {
            final String propertyName = in.nextName();
            switch (propertyName)
            {
                case "owner":
                    owner = gson.fromJson(in, Named.class);
                    break;
                case "field":
                    field = gson.fromJson(in, Reference.class);
                    break;
                case "getter":
                    getter = gson.fromJson(in, Reference.class);
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();

        if (owner.isEmpty())
        {
            throw new JsonParseException("Field owner is not present or empty");
        }
        if (field == null)
        {
            throw new JsonParseException("Field field is not present or empty");
        }
        if (getter == null)
        {
            throw new JsonParseException("Field getter is not present or empty");
        }

        return RecordMetadataBuilder.create()
          .withOwner(owner)
          .withField(field)
          .withGetter(getter)
          .build();
    }
}
