package org.parchmentmc.feather.io.gson.metadata;

import com.google.gson.*;
import org.parchmentmc.feather.metadata.MethodReference;
import org.parchmentmc.feather.metadata.MethodReferenceBuilder;
import org.parchmentmc.feather.named.Named;

import java.lang.reflect.Type;

public final class MethodReferenceAdapter implements JsonSerializer<MethodReference>, JsonDeserializer<MethodReference>
{
    @Override
    public MethodReference deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
    {
        if (!json.isJsonObject())
            throw new JsonParseException("MethodReference needs to be an Object");

        final JsonObject source = json.getAsJsonObject();

        final MethodReferenceBuilder builder = MethodReferenceBuilder.create();

        builder.withOwner(context.deserialize(source.get("owner"), Named.class));
        builder.withName(context.deserialize(source.get("name"), Named.class));
        builder.withDescriptor(context.deserialize(source.get("descriptor"), Named.class));
        builder.withSignature(context.deserialize(source.get("signature"), Named.class));

        return builder.build();
    }

    @Override
    public JsonElement serialize(final MethodReference src, final Type typeOfSrc, final JsonSerializationContext context)
    {
        final JsonObject target = new JsonObject();

        target.add("owner", context.serialize(src.getOwner()));
        target.add("name", context.serialize(src.getName()));
        target.add("descriptor", context.serialize(src.getDescriptor()));
        target.add("signature", context.serialize(src.getSignature()));

        return target;
    }
}
