package org.parchmentmc.feather.io.gson.metadata;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.parchmentmc.feather.metadata.ClassMetadata;
import org.parchmentmc.feather.metadata.FieldMetadata;
import org.parchmentmc.feather.metadata.ImmutableClassMetadata;
import org.parchmentmc.feather.metadata.MethodMetadata;
import org.parchmentmc.feather.named.ImmutableNamed;
import org.parchmentmc.feather.named.Named;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * GSON adapter for {@link ClassMetadata} objects.
 *
 * <p>For internal use. Users should use {@link MetadataAdapterFactory} instead.</p>
 */
class ClassMetadataAdapter extends TypeAdapter<ClassMetadata> {
    private static final TypeToken<Set<Named>> NAMED_Set_TOKEN = new TypeToken<Set<Named>>() {
    };
    private static final TypeToken<Set<MethodMetadata>> METHOD_METADATA_Set_TOKEN = new TypeToken<Set<MethodMetadata>>() {
    };
    private static final TypeToken<Set<FieldMetadata>> FIELD_METADATA_Set_TOKEN = new TypeToken<Set<FieldMetadata>>() {
    };
    private static final TypeToken<Set<ClassMetadata>> CLASS_METADATA_Set_TOKEN = new TypeToken<Set<ClassMetadata>>() {
    };

    private final Gson gson;

    public ClassMetadataAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, ClassMetadata value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        out.name("name");
        gson.toJson(value.getName(), Named.class, out);
        out.name("owner");
        gson.toJson(value.getOwner(), Named.class, out);
        out.name("security").value(value.getSecuritySpecification());
        out.name("extends");
        gson.toJson(value.getSuperName(), Named.class, out);
        out.name("implements");
        gson.toJson(value.getInterfaces(), NAMED_Set_TOKEN.getType(), out);
        out.name("fields");
        gson.toJson(value.getFields(), FIELD_METADATA_Set_TOKEN.getType(), out);
        out.name("methods");
        gson.toJson(value.getMethods(), METHOD_METADATA_Set_TOKEN.getType(), out);
        out.name("inner");
        gson.toJson(value.getInnerClasses(), CLASS_METADATA_Set_TOKEN.getType(), out);
        out.endObject();
    }

    @Override
    public ClassMetadata read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        Named name = ImmutableNamed.empty();
        Named owner = ImmutableNamed.empty();
        int security = -1;
        Named superName = ImmutableNamed.empty();
        LinkedHashSet<Named> interfaces = null;
        LinkedHashSet<FieldMetadata> fields = null;
        LinkedHashSet<MethodMetadata> methods = null;
        LinkedHashSet<ClassMetadata> innerClasses = null;

        in.beginObject();
        while (in.hasNext()) {
            final String propertyName = in.nextName();
            switch (propertyName) {
                case "name":
                    name = gson.fromJson(in, Named.class);
                    break;
                case "owner":
                    owner = gson.fromJson(in, Named.class);
                    break;
                case "security":
                    security = in.nextInt();
                    break;
                case "extends":
                    superName = gson.fromJson(in, Named.class);
                    break;
                case "implements":
                    interfaces = gson.fromJson(in, NAMED_Set_TOKEN.getType());
                    break;
                case "fields":
                    fields = gson.fromJson(in, FIELD_METADATA_Set_TOKEN.getType());
                    break;
                case "methods":
                    methods = gson.fromJson(in, METHOD_METADATA_Set_TOKEN.getType());
                    break;
                case "inner":
                    innerClasses = gson.fromJson(in, CLASS_METADATA_Set_TOKEN.getType());
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();

        if (name.isEmpty()) throw new JsonParseException("Class metadata name is not present or empty");
        // owner can be empty
        if (security == -1) throw new JsonParseException("Class metadata security specification is not present");
        // superName can be empty
        if (interfaces == null) methods = new LinkedHashSet<>();
        if (fields == null) methods = new LinkedHashSet<>();
        if (methods == null) methods = new LinkedHashSet<>();
        if (innerClasses == null) innerClasses = new LinkedHashSet<>();

        return new ImmutableClassMetadata(superName, interfaces, methods, fields, innerClasses, owner, name, security);
    }
}
