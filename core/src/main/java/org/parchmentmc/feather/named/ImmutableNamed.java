package org.parchmentmc.feather.named;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Objects;

public class ImmutableNamed implements Named {
    // A shared empty immutable singleton.
    private static final ImmutableNamed EMPTY = new ImmutableNamed(ImmutableMap.of());

    public static ImmutableNamed empty() {
        return EMPTY;
    }

    public static ImmutableNamed of(String mappingSchema, String mappingValue) {
        return new ImmutableNamed(ImmutableMap.of(mappingSchema, mappingValue));
    }

    public static ImmutableNamed of(Map<String, String> names) {
        return new ImmutableNamed(names);
    }

    private final Map<String, String> names;

    private ImmutableNamed(final Map<String, String> names) {
        this.names = ImmutableMap.copyOf(names);
    }

    @Override
    public Map<String, String> getNames() {
        return names;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Named)) return false;
        Named that = (Named) o;
        return getNames().equals(that.getNames());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNames());
    }
}
