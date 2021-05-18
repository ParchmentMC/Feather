package org.parchmentmc.feather.metadata;

import org.parchmentmc.feather.util.SimpleVersion;

import java.util.List;
import java.util.Objects;

public final class SourceMetadataBuilder implements SourceMetadata {
    private SimpleVersion specVersion;
    private String minecraftVersion;
    private List<ClassMetadata> classes;

    private SourceMetadataBuilder() {
    }

    public static SourceMetadataBuilder create() {
        return new SourceMetadataBuilder();
    }

    public SourceMetadataBuilder withSpecVersion(SimpleVersion specVersion) {
        this.specVersion = specVersion;
        return this;
    }

    public SourceMetadataBuilder withMinecraftVersion(String minecraftVersion) {
        this.minecraftVersion = minecraftVersion;
        return this;
    }

    public SourceMetadataBuilder withClasses(List<ClassMetadata> classes) {
        this.classes = classes;
        return this;
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
    public List<ClassMetadata> getClasses() {
        return classes;
    }

    public ImmutableSourceMetadata build() {
        return new ImmutableSourceMetadata(specVersion, minecraftVersion, classes);
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
