package org.parchmentmc.feather.utils;

import java.util.Optional;
import java.util.function.Function;

public final class RemappableType {

    private final String type;

    public RemappableType(final String type) {this.type = type;}

    public String getType() {
        return type;
    }

    public RemappableType remap(
      final Function<String, Optional<String>> remappingFunction
    ) {
        final Optional<String> remappedTyped = remapType(getType(), remappingFunction);
        return new RemappableType(remappedTyped.orElseGet(this::getType));
    }

    private static Optional<String> remapType(
      final String clz,
      Function<String, Optional<String>> remappingFunction
    )
    {
        if (clz.length() == 1)
            return Optional.of(clz); //Early bail out for primitives.

        if (clz.startsWith("["))
            return remapType(clz.substring(1), remappingFunction).map(t -> "[" + t); //Handles arrays.

        if (clz.startsWith("L"))
            return remapType(clz.substring(1), remappingFunction).map(t -> "L" + t); // Handles class prefixes.

        return remappingFunction.apply(clz.replace(";", "")).map(t -> t + ";");
    }

    @Override
    public String toString()
    {
        return getType();
    }
}