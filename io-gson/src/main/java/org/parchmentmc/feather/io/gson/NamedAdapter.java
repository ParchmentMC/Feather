package org.parchmentmc.feather.io.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.named.NamedBuilder;

import java.io.IOException;
import java.util.Map;

/**
 * GSON adapter for {@link Named} objects.
 */
public class NamedAdapter extends TypeAdapter<Named> {
    @Override
    public void write(JsonWriter out, Named value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        for (final Map.Entry<String, String> schemaNameEntry : value.getNames().entrySet()) {
            out.name(schemaNameEntry.getKey()).value(schemaNameEntry.getValue());
        }
        out.endObject();
    }

    @Override
    public Named read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        NamedBuilder builder = NamedBuilder.create();
        in.beginObject();
        while (in.hasNext()) {
            builder.with(in.nextName(), in.nextString());
        }
        in.endObject();

        return builder.build();
    }
}
