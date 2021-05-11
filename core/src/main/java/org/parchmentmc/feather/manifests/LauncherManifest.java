package org.parchmentmc.feather.manifests;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The launcher manifest, version 2.
 *
 * <p>Retrievable from <tt>https://launchermeta.mojang.com/mc/game/version_manifest_v2.json</tt>.</p>
 *
 * @see <a href="https://minecraft.fandom.com/wiki/Version_manifest.json">Official Minecraft wiki, <tt>Version_manifest.json</tt></a>
 */
public class LauncherManifest implements Serializable {
    /**
     * The latest version information.
     */
    private final LatestVersionInfo latest;
    /**
     * The list of Minecraft version data.
     */
    private final List<VersionData> versions;

    public LauncherManifest(LatestVersionInfo latest, List<VersionData> versions) {
        this.latest = latest;
        this.versions = Collections.unmodifiableList(new ArrayList<>(versions));
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

    /**
     * The information for the latest release and snapshot versions.
     */
    public static class LatestVersionInfo implements Serializable {
        /**
         * The ID of the latest Minecraft release version.
         */
        private final String release;
        /**
         * The ID of the latest Minecraft snapshot version.
         */
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
    }

    /**
     * The version data for a specific Minecraft version.
     */
    public static class VersionData implements Serializable {
        /**
         * The ID of the Minecraft version associated with the data.
         */
        private final String id;
        /**
         * The type of release for this version.
         */
        private final String type;
        /**
         * The URL where the manifest for this version can be downloaded from.
         */
        private final String url;
        /**
         * The (presumably) last date and time the manifest for the version was modified or updated.
         */
        private final OffsetDateTime time;
        /**
         * The date and time when this version was publicly released.
         */
        private final OffsetDateTime releaseTime;
        /**
         * The SHA-1 checksum of the manifest for this version.
         */
        private final String sha1;
        /**
         * The safety compliance level of this version.
         */
        private final int complianceLevel;

        public VersionData(String id, String type, String url, OffsetDateTime time, OffsetDateTime releaseTime, String sha1, int complianceLevel) {
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
         * <p>This is used by the launcher to categorize old or snapshot versions. As of 21w15a, there are 4 known values:
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
         * </p>
         *
         * @return the type of release
         */
        public String getType() {
            return type;
        }

        /**
         * Returns the URL where the manifest for this version can be downloaded from.
         *
         * <p>The current format is <tt>https://launchermeta.mojang.com/v1/packages/<strong>&lt;SHA-1 checksum of manifest></strong>/<strong><{@link #id}></strong>.json</tt></p>
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
    }
}
