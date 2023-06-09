package org.parchmentmc.feather.io.moshi.spi;

import java.io.IOException;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.parchmentmc.feather.io.moshi.LinkedHashSetMoshiAdapter;
import org.parchmentmc.feather.io.moshi.MDCMoshiAdapter;
import org.parchmentmc.feather.io.moshi.MetadataMoshiAdapter;
import org.parchmentmc.feather.io.moshi.OffsetDateTimeAdapter;
import org.parchmentmc.feather.io.moshi.SimpleVersionAdapter;
import org.parchmentmc.feather.spi.IOAdapter;
import org.parchmentmc.feather.spi.IOAdapterFactory;

public class MoshiAdapterFactory implements IOAdapterFactory {

    private static final Moshi moshi = new Moshi.Builder()
        .add(LinkedHashSetMoshiAdapter.FACTORY)
        .add(new MDCMoshiAdapter())
        .add(new MetadataMoshiAdapter())
        .add(new OffsetDateTimeAdapter())
        .add(new SimpleVersionAdapter())
        .build();

    @Override
    public <T> IOAdapter<T> create(Class<T> clazz) {
        return new MoshiWrapper<>(moshi.adapter(clazz));
    }

    static class MoshiWrapper<T> implements IOAdapter<T> {

        private final JsonAdapter<T> adapter;

        MoshiWrapper(JsonAdapter<T> adapter) {
            this.adapter = adapter;
        }

        @Override
        public String name() {
            return "moshi";
        }

        @Override
        public T fromJson(String input) throws IOException {
            return adapter.fromJson(input);
        }

        @Override
        public String toJson(T value) {
            return adapter.toJson(value);
        }
    }

}
