package org.parchmentmc.feather.io.gson.metadata;

import com.google.gson.*;
import org.parchmentmc.feather.metadata.FieldMetadata;
import org.parchmentmc.feather.metadata.FieldMetadataBuilder;
import org.parchmentmc.feather.named.Named;

import java.lang.reflect.Type;

public final class FieldMetadataAdapter implements JsonSerializer<FieldMetadata>, JsonDeserializer<FieldMetadata>
{
    @Override
    public FieldMetadata deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
    {
        if (!json.isJsonObject())
            throw new JsonParseException("FieldMetadata needs to be an object");

        final JsonObject source = json.getAsJsonObject();
        final FieldMetadataBuilder builder = FieldMetadataBuilder.create();

        builder.withOwner(context.deserialize(source.get("owner"), Named.class));
        builder.withName(context.deserialize(source.get("name"), Named.class));
        builder.withDescriptor(context.deserialize(source.get("descriptor"), Named.class));
        builder.withSignature(context.deserialize(source.get("signature"), Named.class));
        builder.withSecuritySpecification(source.get("security").getAsInt());

        return builder.build();
    }

    @Override
    public JsonElement serialize(final FieldMetadata src, final Type typeOfSrc, final JsonSerializationContext context)
    {
        final JsonObject target = new JsonObject();

        target.add("owner", context.serialize(src.getOwner()));
        target.add("name", context.serialize(src.getName()));
        target.add("descriptor", context.serialize(src.getDescriptor()));
        target.add("signature", context.serialize(src.getSignature()));
        target.addProperty("security", src.getSecuritySpecification());

        return target;
    }
}
