package org.parchmentmc.feather.io.gson.metadata;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.parchmentmc.feather.metadata.FieldMetadata;
import org.parchmentmc.feather.metadata.SourceMetadata;
import org.parchmentmc.feather.metadata.SourceMetadataBuilder;
import org.parchmentmc.feather.util.SimpleVersion;

import java.lang.reflect.Type;
import java.util.List;

public final class SourceMetadataAdapter implements JsonSerializer<SourceMetadata>, JsonDeserializer<SourceMetadata>
{
    private static final TypeToken<List<FieldMetadata>> LIST_OF_CLASS_METADATA_TYPE = new TypeToken<List<FieldMetadata>>() {};

    @Override
    public SourceMetadata deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
    {
        if (!json.isJsonObject())
            throw new JsonParseException("SourceMetadata needs to be an object");

        final JsonObject source = json.getAsJsonObject();
        final SourceMetadataBuilder builder = SourceMetadataBuilder.create();

        builder.withSpecVersion(context.deserialize(source.get("specVersion"), SimpleVersion.class));
        builder.withMinecraftVersion(source.get("minecraftVersion").getAsString());
        builder.withClasses(context.deserialize(source.get("classes"), LIST_OF_CLASS_METADATA_TYPE.getType()));

        return builder.build();
    }

    @Override
    public JsonElement serialize(final SourceMetadata src, final Type typeOfSrc, final JsonSerializationContext context)
    {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.add("specVersion", context.serialize(src.getSpecificationVersion(), SimpleVersion.class));
        jsonObject.addProperty("minecraftVersion", src.getMinecraftVersion());
        jsonObject.add("classes", context.serialize(src.getClasses(), LIST_OF_CLASS_METADATA_TYPE.getType()));

        return jsonObject;
    }
}
