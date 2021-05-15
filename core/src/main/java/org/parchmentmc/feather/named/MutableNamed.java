package org.parchmentmc.feather.named;

import com.google.common.collect.Maps;
import org.parchmentmc.feather.util.Constants;

import java.util.Map;

/**
 * A mutable named object.
 */
public class MutableNamed implements Named
{
    private final Map<String, String> names = Maps.newHashMap();

    public MutableNamed(
      final String mappingName,
      final String mappingValue
    )
    {
        this.names.put(mappingName, mappingValue);
    }

    public MutableNamed(
      final Map<String, String> names
    )
    {
        this.names.putAll(names);
    }

    public MutableNamed(
      final Named named
    )
    {
        this.names.putAll(named.getNames());
    }

    public MutableNamed()
    {
    }

    @Override
    public Map<String, String> getNames()
    {
        return names;
    }

    /**
     * Adds a new name with the given schema and name to this named object.
     *
     * @param scheme The schema to add.
     * @param name The name to add.
     * @return This instance.
     */
    public MutableNamed with(
      final String scheme,
      final String name
    ) {
        this.names.put(scheme, name);
        return this;
    }

    /**
     * Adds a new obfuscated name with the given name to this named object.
     *
     * @param name The name to add.
     * @return This instance.
     */
    public MutableNamed withObfuscated(
      final String name
    ) {
        return with(Constants.Names.OBFUSCATED, name);
    }

    /**
     * Adds a new mojang name with the given name to this named object.
     *
     * @param name The name to add.
     * @return This instance.
     */
    public MutableNamed withMojang(
      final String name
    ) {
        return with(Constants.Names.MOJANG, name);
    }

    /**
     * Creates a new immutable snapshot of this mutable named object.
     *
     * @return The immutable snapshot.
     */
    public Named toImmutable() {
        return new ImmutableNamed(this.names);
    }
}
