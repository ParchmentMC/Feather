package org.parchmentmc.feather.util;

import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.named.NamedBuilder;

public final class TestConstantsHelper {
    public static Named named(final String obfuscatedName) {
        return NamedBuilder.create().withObfuscated(obfuscatedName).build();
    }

    public static Named named(final String obfuscatedName, final String mojangName) {
        return NamedBuilder.create()
                .withObfuscated(obfuscatedName)
                .withMojang(mojangName)
                .build();
    }
}
