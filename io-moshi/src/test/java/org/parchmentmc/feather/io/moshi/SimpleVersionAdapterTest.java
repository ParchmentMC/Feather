package org.parchmentmc.feather.io.moshi;

import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.util.SimpleVersion;

public class SimpleVersionAdapterTest extends MoshiTest {
    public SimpleVersionAdapterTest() {
        super(b -> b.add(new SimpleVersionAdapter()));
    }

    @Test
    public void testMoshi() {
        test(SimpleVersion.class, SimpleVersion.of(1, 2, 3));
        test(SimpleVersion.class, SimpleVersion.of("1.4"));
    }
}
