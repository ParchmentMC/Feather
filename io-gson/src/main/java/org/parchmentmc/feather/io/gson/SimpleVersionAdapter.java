package org.parchmentmc.feather.io.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.parchmentmc.feather.util.SimpleVersion;

import java.io.IOException;

/**
 * GSON adapter for {@link SimpleVersion}s.
 */
public class SimpleVersionAdapter extends TypeAdapter<SimpleVersion> {
    @Override
    public void write(JsonWriter out, SimpleVersion value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.toString());
    }

    @Override
    public SimpleVersion read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return SimpleVersion.of(in.nextString());
    }
}
