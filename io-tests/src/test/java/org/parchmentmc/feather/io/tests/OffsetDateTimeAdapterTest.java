package org.parchmentmc.feather.io.tests;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.io.moshi.MoshiTest;
import org.parchmentmc.feather.io.moshi.OffsetDateTimeAdapter;

public class OffsetDateTimeAdapterTest extends RoundRobinTest {

    @Test
    public void test() {
        test(OffsetDateTime.class, OffsetDateTime.MIN);
        test(OffsetDateTime.class, OffsetDateTime.MAX);
        test(OffsetDateTime.class, OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC));
    }
}
