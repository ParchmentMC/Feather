package org.parchmentmc.feather.io.gson;

import com.google.gson.*;
import org.parchmentmc.feather.util.SimpleVersion;

import java.lang.reflect.Type;

/**
 * Gson adapter for {@link SimpleVersion}.
 */
public final class SimpleVersionHandler implements JsonSerializer<SimpleVersion>, JsonDeserializer<SimpleVersion>
{
    @Override
    public SimpleVersion deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
    {
        if (json.isJsonPrimitive())
            throw new JsonParseException("SimpleVersion needs to be a string");

        return new SimpleVersion(json.getAsString());
    }

    @Override
    public JsonElement serialize(final SimpleVersion src, final Type typeOfSrc, final JsonSerializationContext context)
    {
        return new JsonPrimitive(src.toString());
    }
}
