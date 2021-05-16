package org.parchmentmc.feather.io.moshi;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class OffsetDateTimeAdapterTest extends MoshiTest {
    public OffsetDateTimeAdapterTest() {
        super(b -> b.add(new OffsetDateTimeAdapter()));
    }

    @Test
    public void testGson() {
        test(OffsetDateTime.class, OffsetDateTime.MIN);
        test(OffsetDateTime.class, OffsetDateTime.MAX);
        test(OffsetDateTime.class, OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC));
    }
}
