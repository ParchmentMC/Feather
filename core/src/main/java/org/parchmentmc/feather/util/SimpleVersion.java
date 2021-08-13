package org.parchmentmc.feather.util;

import com.google.common.base.Preconditions;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * A version, represented by three version numbers.
 */
public final class SimpleVersion implements Serializable, Comparable<SimpleVersion> {
    private final int major;
    private final int minor;
    private final int patch;

    public static SimpleVersion of(String version) {
        String[] split = version.split("\\.");
        Preconditions.checkArgument(split.length >= 2, "Expected at least 2 tokens for version %s", version);
        Preconditions.checkArgument(split.length <= 3, "Expected at most 3 tokens for version %s" + version);

        int major = Integer.parseInt(split[0]);
        int minor = Integer.parseInt(split[1]);
        int patch = split.length == 3 ? Integer.parseInt(split[2]) : 0;

        return SimpleVersion.of(major, minor, patch);
    }

    public static SimpleVersion copyOf(SimpleVersion other) {
        return SimpleVersion.of(other.major, other.minor, other.patch);
    }

    public static SimpleVersion of(int major, int minor, int patch) {
        return new SimpleVersion(major, minor, patch);
    }

    SimpleVersion(int major, int minor, int patch) {
        Preconditions.checkArgument(major >= 0, "Major version %s must not be negative", major);
        Preconditions.checkArgument(minor >= 0, "Minor version %s must not be negative", minor);
        Preconditions.checkArgument(patch >= 0, "Patch version %s must not be negative", patch);
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /**
     * Returns the major version number.
     *
     * <p>The major version number is incremented for any backwards-incompatible changes, such as removals in the public
     * API or specification.</p>
     *
     * @return the major version number
     */
    public int getMajor() {
        return major;
    }

    /**
     * Returns the minor version number.
     *
     * <p>The minor version number is incremented for any notable backwards-compatible changes, such as additions
     * in the public API or specification.</p>
     *
     * @return the minor version number
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Returns the patch version number.
     *
     * <p>The patch version number is incremented for any non-noteworthy backwards-compatible changes, such as fixes for
     * bugs or issues.</p>
     *
     * @return the patch version number
     */
    public int getPatch() {
        return patch;
    }

    /**
     * Returns whether the given version is compatible with this version.
     *
     * <p>Two versions are compatible if they have the same major version, regardless of the minor and patch versions.</p>
     *
     * @param o the other version to compare
     * @return if the given version is compatible
     * @see #getMajor()
     */
    public boolean isCompatibleWith(SimpleVersion o) {
        return this.major == o.major;
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }

    @Override
    public int compareTo(SimpleVersion o) {
        int ret = this.major - o.major;
        if (ret != 0) return ret;
        ret = this.minor - o.minor;
        if (ret != 0) return ret;
        return this.patch - o.patch;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleVersion that = (SimpleVersion) o;
        return major == that.major && minor == that.minor && patch == that.patch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch);
    }
}
