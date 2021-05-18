package org.parchmentmc.feather.metadata;

import com.google.common.collect.Lists;
import org.parchmentmc.feather.util.SimpleVersion;

import java.util.List;
import java.util.Objects;

public class ImmutableSourceMetadata implements SourceMetadata
{
    private final SimpleVersion       specVersion;
    private final String              minecraftVersion;
    private final List<ClassMetadata> classes;

    public ImmutableSourceMetadata(final SimpleVersion specVersion, final String minecraftVersion, final List<ClassMetadata> classes)
    {
        this.specVersion = specVersion;
        this.minecraftVersion = minecraftVersion;
        this.classes = classes;
    }

    ImmutableSourceMetadata()
    {
        this(SimpleVersion.of("1.0.0"), "1.0.0", Lists.newArrayList());
    }

    @Override
    public SimpleVersion getSpecificationVersion()
    {
        return specVersion;
    }

    @Override
    public String getMinecraftVersion()
    {
        return minecraftVersion;
    }

    @Override
    public List<ClassMetadata> getClasses()
    {
        return classes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SourceMetadata)) return false;
        SourceMetadata that = (SourceMetadata) o;
        return getSpecificationVersion().equals(that.getSpecificationVersion())
                && getMinecraftVersion().equals(that.getMinecraftVersion())
                && getClasses().equals(that.getClasses());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSpecificationVersion(), getMinecraftVersion(), getClasses());
    }
}
