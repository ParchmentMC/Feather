package org.parchmentmc.feather.io.moshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.util.SimpleVersion;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleVersionAdapterTest {
    private final Moshi moshi = new Moshi.Builder().add(new SimpleVersionAdapter()).build();
    private final JsonAdapter<SimpleVersion> adapter = moshi.adapter(SimpleVersion.class);

    @Test
    public void testMoshi() {
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
