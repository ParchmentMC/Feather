package org.parchmentmc.feather.metadata;

import org.parchmentmc.feather.util.HasImmutable;
import org.parchmentmc.feather.util.SimpleVersion;

import java.util.LinkedHashSet;

/**
 * The metadata of a given minecraft version.
 */
public interface SourceMetadata extends HasImmutable<SourceMetadata> {

    /**
     * The version of the metadata loaded.
     *
     * @return The specification version.
     */
    SimpleVersion getSpecificationVersion();

    /**
     * The minecraft version this metadata belongs to.
     *
     * @return The minecraft version.
     */
    String getMinecraftVersion();

    /**
     * The classes in the source.
     *
     * @return The classes.
     */
    LinkedHashSet<ClassMetadata> getClasses();
}
