package org.parchmentmc.feather.named;

import com.google.common.collect.ImmutableMap;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.LinkedHashMap;
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
        return names.isEmpty() ? empty() : new ImmutableNamed(names);
    }

    private final Map<String, String> names;

    private ImmutableNamed(final Map<String, String> names) {
        this.names = new LinkedHashMap<>(names);
    }

    @Override
    public Map<String, String> getNames() {
        return names;
    }

    @Override
    public @NonNull Named toImmutable()
    {
        return this;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof ImmutableNamed))
        {
            return false;
        }
        final ImmutableNamed that = (ImmutableNamed) o;
        return Objects.equals(getNames(), that.getNames());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getNames());
    }

    @Override
    public String toString()
    {
        return "ImmutableNamed{" +
                 "names=" + names +
                 '}';
    }
}
