package org.parchmentmc.feather.io.gson.metadata;

import com.google.gson.*;
import org.parchmentmc.feather.named.NamedBuilder;
import org.parchmentmc.feather.named.Named;

import java.lang.reflect.Type;
import java.util.Map;

public final class NamedHandler implements JsonSerializer<Named>, JsonDeserializer<Named>
{
    @Override
    public Named deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
    {
        if (!json.isJsonObject())
            throw new JsonParseException("Named needs to be an object.");

        final JsonObject source = json.getAsJsonObject();
        final NamedBuilder builder = NamedBuilder.create();

        for (Map.Entry<String, JsonElement> e : source.entrySet())
        {
            if (!e.getValue().isJsonPrimitive())
                throw new JsonParseException("The value of a named schema entry needs to be a string");

            builder.with(e.getKey(), e.getValue().getAsString());
        }

        return builder.build();
    }

    @Override
    public JsonElement serialize(final Named src, final Type typeOfSrc, final JsonSerializationContext context)
    {
        final JsonObject target = new JsonObject();

        src.getNames().forEach(target::addProperty);

        return target;
    }
}
