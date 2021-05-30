package org.parchmentmc.feather.io.gson.metadata;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.parchmentmc.feather.metadata.ClassMetadata;
import org.parchmentmc.feather.metadata.SourceMetadata;
import org.parchmentmc.feather.metadata.SourceMetadataBuilder;
import org.parchmentmc.feather.util.SimpleVersion;

import java.io.IOException;
import java.util.LinkedHashSet;

/**
 * GSON adapter for {@link SourceMetadata} objects.
 *
 * <p>For internal use. Users should use {@link MetadataAdapterFactory} instead.</p>
 */
class SourceMetadataAdapter extends TypeAdapter<SourceMetadata> {
    private static final TypeToken<LinkedHashSet<ClassMetadata>> CLASS_METADATA_Set_TOKEN = new TypeToken<LinkedHashSet<ClassMetadata>>() {
    };

    private final Gson gson;

    public SourceMetadataAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, SourceMetadata value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        out.name("specVersion");
        gson.toJson(value.getSpecificationVersion(), SimpleVersion.class, out);
        out.name("minecraftVersion").value(value.getMinecraftVersion());
        out.name("classes");
        gson.toJson(value.getClasses(), CLASS_METADATA_Set_TOKEN.getType(), out);
        out.endObject();
    }

    @Override
    public SourceMetadata read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        SimpleVersion specVersion = null;
        String minecraftVersion = null;
        LinkedHashSet<ClassMetadata> classes = null;

        in.beginObject();
        while (in.hasNext()) {
            final String name = in.nextName();
            switch (name) {
                case "specVersion":
                    specVersion = gson.fromJson(in, SimpleVersion.class); // TODO: version checking
                    break;
                case "minecraftVersion":
                    minecraftVersion = in.nextString();
                    break;
                case "classes":
                    classes = gson.fromJson(in, CLASS_METADATA_Set_TOKEN.getType());
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();

        if (specVersion == null) throw new JsonParseException("Specification version is not present");
        if (minecraftVersion == null) throw new JsonParseException("Minecraft version is not present");
        if (classes == null) throw new JsonParseException("Classes Set is not present");

        return SourceMetadataBuilder.create()
          .withMinecraftVersion(minecraftVersion)
          .withSpecVersion(specVersion)
          .withClasses(classes)
          .build();
    }
}
