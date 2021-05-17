package org.parchmentmc.feather.io.gson.metadata;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.parchmentmc.feather.metadata.MethodMetadata;
import org.parchmentmc.feather.metadata.MethodMetadataBuilder;
import org.parchmentmc.feather.metadata.MethodReference;
import org.parchmentmc.feather.named.Named;

import java.lang.reflect.Type;
import java.util.List;

public final class MethodMetadataHandler implements JsonSerializer<MethodMetadata>, JsonDeserializer<MethodMetadata>
{
    private static final     TypeToken<List<MethodReference>> LIST_OF_METHOD_REFERENCE_TYPE = new TypeToken<List<MethodReference>>() {};

    @Override
    public MethodMetadata deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
    {
        if (!json.isJsonObject())
            throw new JsonParseException("MethodMetadata needs to be an object");

        final JsonObject source = json.getAsJsonObject();
        final MethodMetadataBuilder builder = MethodMetadataBuilder.create();

        builder.withOwner(context.deserialize(source.get("owner"), Named.class));
        builder.withName(context.deserialize(source.get("name"), Named.class));
        builder.withDescriptor(context.deserialize(source.get("descriptor"), Named.class));
        builder.withSignature(context.deserialize(source.get("signature"), Named.class));
        builder.withSecuritySpecification(source.get("security").getAsInt());
        builder.withLambda(source.get("lambda").getAsBoolean());
        builder.withBouncingTarget(context.deserialize(source.get("bouncingTarget"), MethodReference.class));
        builder.withOverrides(context.deserialize(source.get("overrides"), LIST_OF_METHOD_REFERENCE_TYPE.getType()));

        return builder.build();
    }

    @Override
    public JsonElement serialize(final MethodMetadata src, final Type typeOfSrc, final JsonSerializationContext context)
    {
        final JsonObject target = new JsonObject();

        target.add("owner", context.serialize(src.getOwner()));
        target.add("name", context.serialize(src.getName()));
        target.add("descriptor", context.serialize(src.getDescriptor()));
        target.add("signature", context.serialize(src.getSignature()));
        target.addProperty("security", src.getSecuritySpecification());
        target.addProperty("lambda", src.isLambda());
        target.add("bouncingTarget", context.serialize(src.getBouncingTarget(), MethodReference.class));
        target.add("overrides", context.serialize(src.getOverrides(), LIST_OF_METHOD_REFERENCE_TYPE.getType()));

        return target;
    }
}
