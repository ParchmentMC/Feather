package org.parchmentmc.feather.manifests;

import com.google.common.collect.ImmutableMap;
import org.parchmentmc.feather.named.ImmutableNamed;
import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.util.Constants;

final class TestConstantsHelper {
    public static Named named(String value) {
        return ImmutableNamed.of(Constants.Names.OBFUSCATED, value);
    }

    public static Named named(String valueA, String valueB) {
        return ImmutableNamed.of(ImmutableMap.of(Constants.Names.OBFUSCATED, valueA, Constants.Names.MOJANG, valueB));
    }
}
