package org.parchmentmc.feather.io.gson.metadata;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.parchmentmc.feather.metadata.ClassMetadata;
import org.parchmentmc.feather.metadata.ClassMetadataBuilder;
import org.parchmentmc.feather.metadata.FieldMetadata;
import org.parchmentmc.feather.metadata.MethodMetadata;
import org.parchmentmc.feather.named.Named;

import java.lang.reflect.Type;
import java.util.List;

public final class ClassMetadataHandler implements JsonSerializer<ClassMetadata>, JsonDeserializer<ClassMetadata>
{

    private static final TypeToken<List<Named>>          LIST_OF_NAMED_TYPE           = new TypeToken<List<Named>>() {};
    private static final TypeToken<List<MethodMetadata>> LIST_OF_METHOD_METADATA_TYPE = new TypeToken<List<MethodMetadata>>() {};
    private static final TypeToken<List<FieldMetadata>>  LIST_OF_FIELD_METADATA_TYPE  = new TypeToken<List<FieldMetadata>>() {};
    private static final TypeToken<List<ClassMetadata>>  LIST_OF_CLASS_METADATA_TYPE  = new TypeToken<List<ClassMetadata>>() {};

    @Override
    public ClassMetadata deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
    {
        if (!json.isJsonObject())
        {
            throw new JsonParseException("ClassMetadata needs to be an Object");
        }

        final JsonObject source = json.getAsJsonObject();
        final ClassMetadataBuilder builder = ClassMetadataBuilder.create();

        builder.withName(context.deserialize(source.get("name"), Named.class));
        builder.withOwner(context.deserialize(source.get("owner"), Named.class));
        builder.withSuperName(context.deserialize(source.get("extends"), Named.class));
        builder.withSecuritySpecifications(source.get("security").getAsInt());
        builder.withInterfaces(context.deserialize(source.get("implements"), LIST_OF_NAMED_TYPE.getType()));
        builder.withFields(context.deserialize(source.get("fields"), LIST_OF_FIELD_METADATA_TYPE.getType()));
        builder.withMethods(context.deserialize(source.get("methods"), LIST_OF_METHOD_METADATA_TYPE.getType()));
        builder.withInnerClasses(context.deserialize(source.get("inner"), LIST_OF_CLASS_METADATA_TYPE.getType()));

        return builder.build();
    }

    @Override
    public JsonElement serialize(final ClassMetadata src, final Type typeOfSrc, final JsonSerializationContext context)
    {
        final JsonObject target = new JsonObject();

        target.add("name", context.serialize(src.getName(), Named.class));
        target.add("owner", context.serialize(src.getOwner(), Named.class));
        target.add("extends", context.serialize(src.getSuperName(), Named.class));
        target.addProperty("security", src.getSecuritySpecification());
        target.add("implements", context.serialize(src.getInterfaces(), LIST_OF_NAMED_TYPE.getType()));
        target.add("fields", context.serialize(src.getFields(), LIST_OF_FIELD_METADATA_TYPE.getType()));
        target.add("methods", context.serialize(src.getMethods(), LIST_OF_METHOD_METADATA_TYPE.getType()));
        target.add("inner", context.serialize(src.getInnerClasses(), LIST_OF_CLASS_METADATA_TYPE.getType()));

        return target;
    }
}
