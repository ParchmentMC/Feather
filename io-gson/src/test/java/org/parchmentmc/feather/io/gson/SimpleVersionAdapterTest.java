package org.parchmentmc.feather.io.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.util.SimpleVersion;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleVersionAdapterTest {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(SimpleVersion.class, new SimpleVersionAdapter()).create();
    private final TypeAdapter<SimpleVersion> adapter = gson.getAdapter(SimpleVersion.class);

    @Test
    public void testGson() {
        test(SimpleVersion.of(1, 2, 3));
        test(SimpleVersion.of("1.4"));
    }

    void test(SimpleVersion original) {
        final String originalJson = assertDoesNotThrow(() -> adapter.toJson(original));

        final SimpleVersion versionA = assertDoesNotThrow(() -> adapter.fromJson(originalJson));
        assertNotNull(versionA);

        final String versionAJson = assertDoesNotThrow(() -> adapter.toJson(versionA));

        final SimpleVersion versionB = assertDoesNotThrow(() -> adapter.fromJson(versionAJson));
        assertNotNull(versionB);

        assertEquals(original, versionA);
        assertEquals(versionA, versionB);

        assertNotSame(original, versionA);
        assertNotSame(versionA, versionB);

        assertEquals(originalJson, versionAJson);
    }
}
