package org.parchmentmc.feather.named;

import com.google.common.collect.Maps;
import org.parchmentmc.feather.util.Constants;

import java.util.Collections;
import java.util.Map;

public class ImmutableNamed implements Named
{
    private final Map<String, String> names = Maps.newHashMap();

    public ImmutableNamed(
      final String mappingName,
      final String mappingValue
    )
    {
        this.names.put(mappingName, mappingValue);
    }

    public ImmutableNamed(
      final Map<String, String> names
    )
    {
        this.names.putAll(names);
    }

    public ImmutableNamed()
    {
    }

    @Override
    public Map<String, String> getNames()
    {
        return Collections.unmodifiableMap(names);
    }
}
