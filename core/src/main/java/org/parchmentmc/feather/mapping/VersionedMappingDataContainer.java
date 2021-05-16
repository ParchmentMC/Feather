package org.parchmentmc.feather.mapping;

import org.parchmentmc.feather.util.SimpleVersion;

/**
 * A versioned specialization of {@link MappingDataContainer}.
 *
 * <p>Because of the nature of libraries, the in-code representation of {@code MappingDataContainer}s will change over
 * time to suit the needs of the users, either adding new features or enhancements, or deprecating then removing
 * outdated features.</p>
 *
 * <p>However, a problem exists in that different users may use different versions of the library, which may or may not
 * include changes to the representation of mapping data containers. Additionally, the serialized data they consume may
 * or may not be in a data format compatible with the data format recognized and understood by their library.</p>
 *
 * <p>This interface extends the specification of the mapping data container to have a <b>format version</b>. This
 * format version (represented by the {@link SimpleVersion} class) is incremented for each change done to the mapping
 * data container format, loosely following the <a href="https://semver.org">Semantic Versioning</a> specification.</p>
 */
public interface VersionedMappingDataContainer extends MappingDataContainer {
    /**
     * The current recognized format version by this library.
     */
    SimpleVersion CURRENT_FORMAT = SimpleVersion.of(1, 1, 0);

    /**
     * Returns the format version of this container.
     *
     * <p>The format version is used in JSON serialization to ensure that a serialized mapping data container is in a
     * format recognized by the program.</p>
     *
     * @return the format version of this container
     * @see #CURRENT_FORMAT
     */
    SimpleVersion getFormatVersion();
}
