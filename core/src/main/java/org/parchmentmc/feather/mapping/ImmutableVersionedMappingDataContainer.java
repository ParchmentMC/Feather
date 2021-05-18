package org.parchmentmc.feather.mapping;

import org.parchmentmc.feather.util.SimpleVersion;

import java.util.Collection;

/**
 * A subclass of {@link ImmutableMappingDataContainer} which contains a format version.
 *
 * @see VersionedMappingDataContainer
 */
public class ImmutableVersionedMappingDataContainer extends ImmutableMappingDataContainer implements VersionedMappingDataContainer {
    private final SimpleVersion version;

    public ImmutableVersionedMappingDataContainer(SimpleVersion version, Collection<? extends PackageData> packages, Collection<? extends ClassData> classes) {
        super(packages, classes);
        this.version = version;
    }

    @Override
    public SimpleVersion getFormatVersion() {
        return version;
    }
}
