package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.util.SimpleVersion;

import java.util.LinkedHashSet;
import java.util.Objects;

public class ImmutableSourceMetadata implements SourceMetadata {
    private final SimpleVersion specVersion;
    private final String minecraftVersion;
    private final LinkedHashSet<ClassMetadata> classes;

    public ImmutableSourceMetadata(final SimpleVersion specVersion, final String minecraftVersion, final LinkedHashSet<ClassMetadata> classes) {
        this.specVersion = specVersion;
        this.minecraftVersion = minecraftVersion;
        this.classes = new LinkedHashSet<>(classes);
    }

    ImmutableSourceMetadata() {
        this(SimpleVersion.of("1.0.0"), "1.0.0", new LinkedHashSet<>());
    }

    @Override
    public SimpleVersion getSpecificationVersion() {
        return specVersion;
    }

    @Override
    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    @Override
    public LinkedHashSet<ClassMetadata> getClasses() {
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

    @Override
    public String toString()
    {
        return "ImmutableSourceMetadata{" +
                 "specVersion=" + specVersion +
                 ", minecraftVersion='" + minecraftVersion + '\'' +
                 ", classes=" + classes +
                 '}';
    }

    @Override
    public @NonNull SourceMetadata toImmutable()
    {
        return this;
    }
}
