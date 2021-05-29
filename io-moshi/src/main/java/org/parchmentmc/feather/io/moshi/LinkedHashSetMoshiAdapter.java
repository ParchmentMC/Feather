package org.parchmentmc.feather.io.moshi;

import com.squareup.moshi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;

public class LinkedHashSetMoshiAdapter<T> extends JsonAdapter<LinkedHashSet<T>> {
    public static final JsonAdapter.Factory FACTORY =
            (type, annotations, moshi) -> {
                Class<?> rawType = Types.getRawType(type);
                if (!annotations.isEmpty()) return null;
                if (rawType == LinkedHashSet.class) {
                    return newLinkedHashSetAdapter(type, moshi).nullSafe();
                }
                return null;
            };


    static <T> JsonAdapter<LinkedHashSet<T>> newLinkedHashSetAdapter(Type type, Moshi moshi) {
        Type elementType = Types.collectionElementType(type, Collection.class);
        JsonAdapter<T> elementAdapter = moshi.adapter(elementType);
        return new LinkedHashSetMoshiAdapter<>(elementAdapter);
    }

    private final JsonAdapter<T> elementAdapter;

    public LinkedHashSetMoshiAdapter(final JsonAdapter<T> elementAdapter) {
        this.elementAdapter = elementAdapter;
    }

    @Nullable
    @Override
    public LinkedHashSet<T> fromJson(final JsonReader reader) throws IOException {
        if (reader.peek() == JsonReader.Token.NULL)
            return null;

        LinkedHashSet<T> result = new LinkedHashSet<>();
        reader.beginArray();
        while (reader.hasNext()) {
            result.add(this.elementAdapter.fromJson(reader));
        }
        reader.endArray();
        return result;
    }

    @Override
    public void toJson(@NotNull final JsonWriter writer, @Nullable final LinkedHashSet<T> value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }

        writer.beginArray();
        for (T element : value) {
            elementAdapter.toJson(writer, element);
        }
        writer.endArray();
    }
}
