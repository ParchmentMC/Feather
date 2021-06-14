package org.parchmentmc.feather.io.tests;

import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.io.moshi.MoshiTest;
import org.parchmentmc.feather.io.moshi.SimpleVersionAdapter;
import org.parchmentmc.feather.util.SimpleVersion;

public class SimpleVersionAdapterTest extends RoundRobinTest {

    @Test
    public void test() {
        test(SimpleVersion.class, SimpleVersion.of(1, 2, 3));
        test(SimpleVersion.class, SimpleVersion.of("1.4"));
    }
}
