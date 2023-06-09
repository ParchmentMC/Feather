package org.parchmentmc.feather.io.tests;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class OffsetDateTimeAdapterTest extends RoundRobinTest {

    @Test
    public void test() {
        test(OffsetDateTime.class, OffsetDateTime.MIN);
        test(OffsetDateTime.class, OffsetDateTime.MAX);
        test(OffsetDateTime.class, OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC));
    }

}
