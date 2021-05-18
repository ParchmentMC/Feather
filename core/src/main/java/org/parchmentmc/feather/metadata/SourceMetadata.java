package org.parchmentmc.feather.metadata;

import org.parchmentmc.feather.util.SimpleVersion;

import java.util.List;

/**
 * The metadata of a given minecraft version.
 */
public interface SourceMetadata {

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
    List<ClassMetadata> getClasses();
}
