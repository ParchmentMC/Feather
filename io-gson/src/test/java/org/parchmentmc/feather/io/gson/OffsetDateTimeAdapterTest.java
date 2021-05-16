package org.parchmentmc.feather.io.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

public class OffsetDateTimeAdapterTest {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter()).create();
    private final TypeAdapter<OffsetDateTime> adapter = gson.getAdapter(OffsetDateTime.class);

    @Test
    public void testGson() {
        test(OffsetDateTime.MIN);
        test(OffsetDateTime.MAX);
        test(OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC));
    }

    void test(OffsetDateTime original) {
        final String originalJson = assertDoesNotThrow(() -> adapter.toJson(original));

        final OffsetDateTime versionA = assertDoesNotThrow(() -> adapter.fromJson(originalJson));
        assertNotNull(versionA);

        final String versionAJson = assertDoesNotThrow(() -> adapter.toJson(versionA));

        final OffsetDateTime versionB = assertDoesNotThrow(() -> adapter.fromJson(versionAJson));
        assertNotNull(versionB);

        assertEquals(original, versionA);
        assertEquals(versionA, versionB);

        assertNotSame(original, versionA);
        assertNotSame(versionA, versionB);

        assertEquals(originalJson, versionAJson);
    }
}
