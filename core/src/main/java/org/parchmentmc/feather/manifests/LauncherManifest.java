package org.parchmentmc.feather.manifests;

import com.google.common.collect.ImmutableList;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

/**
 * The launcher manifest, version 2.
 *
 * <p>Retrievable from <tt>https://launchermeta.mojang.com/mc/game/version_manifest_v2.json</tt>.</p>
 *
 * @see <a href="https://minecraft.fandom.com/wiki/Version_manifest.json">Official Minecraft wiki,
 * <tt>Version_manifest.json</tt></a>
 */
public class LauncherManifest implements Serializable {
    private final LatestVersionInfo latest;
    private final List<VersionData> versions;

    public LauncherManifest(LatestVersionInfo latest, List<VersionData> versions) {
        this.latest = latest;
        this.versions = ImmutableList.copyOf(versions);
    }

    /**
     * Returns the latest version information.
     *
     * @return the latest version information
     */
    public LatestVersionInfo getLatest() {
        return latest;
    }

    /**
     * Returns the list of Minecraft version data.
     *
     * @return the list of Minecraft version data.
     */
    public List<VersionData> getVersions() {
        return versions;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LauncherManifest that = (LauncherManifest) o;
        return getLatest().equals(that.getLatest()) && getVersions().equals(that.getVersions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLatest(), getVersions());
    }

    /**
     * The information for the latest release and snapshot versions.
     */
    public static class LatestVersionInfo implements Serializable {
        private final String release;
        private final String snapshot;

        public LatestVersionInfo(String release, String snapshot) {
            this.release = release;
            this.snapshot = snapshot;
        }

        /**
         * Returns the ID of the latest Minecraft release version.
         *
         * @return the latest release version ID
         */
        public String getRelease() {
            return release;
        }

        /**
         * Returns the ID of the latest Minecraft snapshot version.
         *
         * @return the latest snapshot version ID
         */
        public String getSnapshot() {
            return snapshot;
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LatestVersionInfo that = (LatestVersionInfo) o;
            return getRelease().equals(that.getRelease()) && getSnapshot().equals(that.getSnapshot());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getRelease(), getSnapshot());
        }
    }

    /**
     * The version data for a specific Minecraft version.
     */
    public static class VersionData implements Serializable {
        private final String id;
        private final String type;
        private final String url;
        private final OffsetDateTime time;
        private final OffsetDateTime releaseTime;
        private final String sha1;
        private final int complianceLevel;

        public VersionData(String id, String type, String url, OffsetDateTime time, OffsetDateTime releaseTime,
                           String sha1, int complianceLevel) {
            this.id = id;
            this.type = type;
            this.url = url;
            this.time = time;
            this.releaseTime = releaseTime;
            this.sha1 = sha1;
            this.complianceLevel = complianceLevel;
        }

        /**
         * Returns the ID of the Minecraft version associated with the data.
         *
         * @return the Minecraft version ID
         */
        public String getId() {
            return id;
        }

        /**
         * Returns the type of release for this version.
         *
         * <p>This is used by the launcher to categorize old or snapshot versions. As of 21w15a, there are 4 known
         * values: </p>
         * <dl>
         *     <dt><strong>{@code release}</strong></dt>
         *     <dd>A full release, such as {@code 1.16.5}.</dd>
         *
         *     <dt><strong>{@code snapshot}</strong></dt>
         *     <dd>An in-development snapshot, such as {@code 21w15a}.</dd>
         *
         *     <dt><strong>{@code old_beta}</strong></dt>
         *     <dd>An old <em>beta</em> version, such as {@code b1.8.1} (for version Beta 1.8.1).</dd>
         *
         *     <dt><strong>{@code old_alpha}</strong></dt>
         *     <dd>An old <em>alpha</em> version, such as {@code a1.2.6} (for version Alpha 1.2.6).</dd>
         * </dl>
         *
         * @return the type of release
         */
        public String getType() {
            return type;
        }

        /**
         * Returns the URL where the manifest for this version can be downloaded from.
         *
         * <p>The current format is <tt>https://launchermeta.mojang.com/v1/packages/<strong>&lt;SHA-1 checksum of
         * manifest&gt;</strong>/<strong>&lt;{@link #id}&gt;</strong>.json</tt></p>
         *
         * @return the manifest URL
         */
        public String getUrl() {
            return url;
        }

        /**
         * Returns the (presumably) last date and time the manifest for the version was modified or updated.
         *
         * @return the (presumably) last modified date and time
         */
        public OffsetDateTime getTime() {
            return time;
        }

        /**
         * Returns the date and time when this version was publicly released.
         *
         * @return the release/publication date and time
         */
        public OffsetDateTime getReleaseTime() {
            return releaseTime;
        }

        /**
         * Returns the SHA-1 checksum of the manifest for this version.
         *
         * @return the SHA-1 checksum of the manifest
         * @see #getUrl()
         */
        public String getSHA1() {
            return sha1;
        }

        /**
         * Returns the safety compliance level of this version.
         *
         * <p>Used by the launcher to inform players if the version supports the most recent player safety features.</p>
         *
         * <p>Currently, all versions below <tt>1.16.4-pre1</tt> have a compliance level of 0, while versions above that
         * have a compliance level of 1.</p>
         *
         * @return the safety compliance level
         */
        public int getComplianceLevel() {
            return complianceLevel;
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            VersionData that = (VersionData) o;
            return getComplianceLevel() == that.getComplianceLevel() && getId().equals(that.getId())
                    && getType().equals(that.getType()) && getUrl().equals(that.getUrl()) && getTime().equals(that.getTime())
                    && getReleaseTime().equals(that.getReleaseTime()) && getSHA1().equals(that.getSHA1());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId(), getType(), getUrl(), getTime(), getReleaseTime(), getSHA1(), getComplianceLevel());
        }
    }
}
