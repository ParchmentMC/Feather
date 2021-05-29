package org.parchmentmc.feather.named;

import com.google.common.collect.Maps;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.util.Constants;

import java.util.Map;
import java.util.Objects;

/**
 * A mutable named object.
 */
public class NamedBuilder implements Named {
    private final Map<String, String> names = Maps.newLinkedHashMap();

    public static NamedBuilder create(final String mappingName, final String mappingValue) {
        return new NamedBuilder(mappingName, mappingValue);
    }

    public static NamedBuilder create(final Map<String, String> names) {
        return new NamedBuilder(names);
    }

    public static NamedBuilder create(final Named named) {
        return new NamedBuilder(named);
    }

    public static NamedBuilder create() {
        return new NamedBuilder();
    }

    private NamedBuilder(final String mappingName, final String mappingValue) {
        this.names.put(mappingName, mappingValue);
    }

    private NamedBuilder(final Map<String, String> names) {
        this.names.putAll(names);
    }

    private NamedBuilder(final Named named) {
        this.names.putAll(named.getNames());
    }

    private NamedBuilder() {
    }

    @Override
    public Map<String, String> getNames() {
        return names;
    }

    /**
     * Adds a new name with the given schema and name to this named object.
     *
     * @param scheme The schema to add.
     * @param name   The name to add.
     * @return This instance.
     */
    public NamedBuilder with(final String scheme, final String name) {
        if (scheme.equals("") || name.equals(""))
            return this;

        this.names.put(scheme, name);
        return this;
    }

    /**
     * Adds a new obfuscated name with the given name to this named object.
     *
     * @param name The name to add.
     * @return This instance.
     */
    public NamedBuilder withObfuscated(final String name) {
        return with(Constants.Names.OBFUSCATED, name);
    }

    /**
     * Adds a new mojang name with the given name to this named object.
     *
     * @param name The name to add.
     * @return This instance.
     */
    public NamedBuilder withMojang(final String name) {
        return with(Constants.Names.MOJANG, name);
    }

    /**
     * Add all the schemas and names from the named object.
     *
     * @param source The named object to add all the schemas and names from.
     * @return The builder.
     */
    public NamedBuilder merge(final Named source) {
        this.names.putAll(source.getNames());
        return this;
    }

    /**
     * Creates a new immutable snapshot of this mutable named object.
     *
     * @return The immutable snapshot.
     */
    public Named build() {
        return this.names.isEmpty() ? ImmutableNamed.empty() : ImmutableNamed.of(this.names);
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

    @Override
    public @NonNull Named toImmutable() {
        return build();
    }
}
