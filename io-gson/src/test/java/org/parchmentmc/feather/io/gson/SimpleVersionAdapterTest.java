package org.parchmentmc.feather.io.gson;

import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.util.SimpleVersion;

public class SimpleVersionAdapterTest extends GSONTest {
    public SimpleVersionAdapterTest() {
        super(b -> b.registerTypeAdapter(SimpleVersion.class, new SimpleVersionAdapter()));
    }

    @Test
    public void testGson() {
        test(SimpleVersion.class, SimpleVersion.of(1, 2, 3));
        test(SimpleVersion.class, SimpleVersion.of("1.4"));
    }
}
