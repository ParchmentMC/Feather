package org.parchmentmc.feather.manifests;

import com.google.common.collect.ImmutableMap;
import org.parchmentmc.feather.named.ImmutableNamed;
import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.util.Constants;

final class TestConstantsHelper {
    public static Named named(String value) {
        return new ImmutableNamed(Constants.Names.OBFUSCATED, value);
    }

    public static Named named(String valueA, String valueB) {
        return new ImmutableNamed(ImmutableMap.of(Constants.Names.OBFUSCATED, valueA, Constants.Names.MOJANG, valueB));
    }
}
