package org.parchmentmc.feather.io.gson.spi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.parchmentmc.feather.io.gson.MDCGsonAdapterFactory;
import org.parchmentmc.feather.io.gson.OffsetDateTimeAdapter;
import org.parchmentmc.feather.io.gson.SimpleVersionAdapter;
import org.parchmentmc.feather.io.gson.metadata.MetadataAdapterFactory;
import org.parchmentmc.feather.spi.IOAdapter;
import org.parchmentmc.feather.spi.IOAdapterFactory;
import org.parchmentmc.feather.util.SimpleVersion;

import java.time.OffsetDateTime;

public class GsonAdapterFactory implements IOAdapterFactory {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(new MDCGsonAdapterFactory())
            .registerTypeAdapterFactory(new MetadataAdapterFactory())
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
            .registerTypeAdapter(SimpleVersion.class, new SimpleVersionAdapter())
            .disableHtmlEscaping()
            .create();

    @Override
    public <T> IOAdapter<T> create(Class<T> clazz) {
        return new GsonWrapper<>(clazz);
    }

    static class GsonWrapper<T> implements IOAdapter<T> {

        private final Class<T> typeClass;

        GsonWrapper(Class<T> typeClass) {
            this.typeClass = typeClass;
        }

        @Override
        public String name() {
            return "gson";
        }

        @Override
        public T fromJson(String input) {
            return gson.fromJson(input, typeClass);
        }

        @Override
        public String toJson(T value) {
            return gson.toJson(value, typeClass);
        }
    }

}
