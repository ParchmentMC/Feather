package org.parchmentmc.feather.manifests;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * The manifest for a game version.
 *
 * @see <a href="https://minecraft.fandom.com/wiki/Client.json">Official Minecraft wiki, <tt>Client.json</tt></a>
 */
public class VersionManifest implements Serializable {
    private final String id;
    private final String type;
    private final int complianceLevel;
    private final int minimumLauncherVersion;
    private final OffsetDateTime time;
    private final OffsetDateTime releaseTime;
    private final String mainClass;
    @Nullable
    private final JavaVersionInfo javaVersionInfo;
    private final String assets;
    private final AssetIndexInfo assetIndex;
    private final Map<String, DownloadInfo> downloads;
    private final List<Library> libraries;
    @Nullable
    private final String minecraftArguments;

    public VersionManifest(String id, String type, int complianceLevel, int minimumLauncherVersion, OffsetDateTime time,
                           OffsetDateTime releaseTime, String mainClass, @Nullable JavaVersionInfo javaVersionInfo,
                           String assets, AssetIndexInfo assetIndex, Map<String, DownloadInfo> downloads,
                           List<Library> libraries, @Nullable String minecraftArguments) {
        this.id = id;
        this.type = type;
        this.complianceLevel = complianceLevel;
        this.minimumLauncherVersion = minimumLauncherVersion;
        this.time = time;
        this.releaseTime = releaseTime;
        this.mainClass = mainClass;
        this.javaVersionInfo = javaVersionInfo;
        this.assets = assets;
        this.assetIndex = assetIndex;
        this.downloads = ImmutableMap.copyOf(downloads);
        this.libraries = ImmutableList.copyOf(libraries);
        this.minecraftArguments = minecraftArguments;
    }

    /**
     * Returns the ID of the Minecraft version associated with the manifest.
     *
     * @return the Minecraft version ID
     * @see LauncherManifest.VersionData#getId()
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the type of release for this version.
     *
     * @return the type of release
     * @see LauncherManifest.VersionData#getType()
     */
    public String getType() {
        return type;
    }

    /**
     * Return the safety compliance level of this version.
     *
     * @return the safety compliance level
     * @see LauncherManifest.VersionData#getComplianceLevel()
     */
    public int getComplianceLevel() {
        return complianceLevel;
    }

    /**
     * Returns the minimum launcher version required to run this version.
     *
     * @return the minimum launcher version
     */
    public int getMinimumLauncherVersion() {
        return minimumLauncherVersion;
    }

    /**
     * Returns the (presumably) last date and time the manifest for the version was modified or updated.
     * TODO: verify this.
     *
     * @return the (presumably) last modified date and time
     * @see LauncherManifest.VersionData#getTime()
     */
    public OffsetDateTime getTime() {
        return time;
    }

    /**
     * Returns the date and time when this version was publicly released.
     *
     * @return the release/publication date and time
     * @see LauncherManifest.VersionData#getReleaseTime()
     */
    public OffsetDateTime getReleaseTime() {
        return releaseTime;
    }

    /**
     * Returns the fully-qualified name of the main class for the game.
     *
     * @return the fully-qualified main class
     */
    public String getMainClass() {
        return mainClass;
    }

    /**
     * Returns the java version on which this game runs on, or {@code null} if none is specified.
     *
     * @return the java version information, or {@code null} if none is specified
     */
    @Nullable
    public JavaVersionInfo getJavaVersionInfo() {
        return javaVersionInfo;
    }

    /**
     * Returns the asset version ID used by this game version.
     *
     * @return the asset version ID
     */
    public String getAssets() {
        return assets;
    }

    /**
     * Returns the information of the asset index used for this game version.
     *
     * @return the asset index information
     */
    public AssetIndexInfo getAssetIndex() {
        return assetIndex;
    }

    /**
     * Returns an immutable map of download keys and their corresponding information.
     *
     * <p>As of 21w15a, there are four known downloads:
     * <dl>
     *     <dt><strong><tt>client</tt></strong></dt>
     *     <dd>The client JAR. <em>Should always be available.</em></dd>
     *     <dt><strong><tt>server</tt></strong></dt>
     *     <dd>The dedicated server JAR.</dd>
     *     <dt><strong><tt>client_mappings</tt></strong></dt>
     *     <dd>The obfuscation map for the client JAR. </dd>
     *     <dt><strong><tt>server_mappings</tt></strong></dt>
     *     <dd>The obfuscation map for the dedicated server JAR.</dd>
     * </dl></p>
     *
     * @return an immutable map of download keys and their information
     */
    public Map<String, DownloadInfo> getDownloads() {
        return downloads;
    }

    /**
     * Returns the immutable list of libraries for this game version.
     *
     * @return the immutable list of libraries
     */
    public List<Library> getLibraries() {
        return libraries;
    }

    /**
     * Returns a string which is passed into the game as arguments, or {@code null} if not specified.
     *
     * <p>Tokens can appear in the string, in the form of <tt>{<em>token name</em>}</tt>. These will be replaced
     * by the launcher with certain information gathered from the running environment.</p>
     *
     * <p>Usually only used by old versions (old_alpha and old_beta).</p>
     *
     * @return a string of game arguments, or {@code null} if not specified
     */
    @Nullable
    public String getMinecraftArguments() {
        return minecraftArguments;
    }

    // TODO: arguments[game,jvm], logging

    /**
     * Information for the java version which a game version runs on.
     */
    public static class JavaVersionInfo implements Serializable {
        private final String component;
        private final int majorVersion;

        public JavaVersionInfo(String component, int majorVersion) {
            this.component = component;
            this.majorVersion = majorVersion;
        }

        public String getComponent() {
            return component;
        }

        public int getMajorVersion() {
            return majorVersion;
        }
    }

    /**
     * Information for a download.
     */
    public static class DownloadInfo implements Serializable {
        private final String sha1;
        private final int size;
        private final String url;

        public DownloadInfo(String sha1, int size, String url) {
            this.sha1 = sha1;
            this.size = size;
            this.url = url;
        }

        /**
         * Returns the SHA-1 checksum of the download.
         *
         * @return the SHA-1 checksum
         */
        public String getSHA1() {
            return sha1;
        }

        /**
         * Returns the size of the download in bytes.
         *
         * @return the size of the download, in bytes
         */
        public int getSize() {
            return size;
        }

        /**
         * Returns the URL where the download is located at.
         *
         * @return the URL of the download
         */
        public String getUrl() {
            return url;
        }
    }

    public static class AssetIndexInfo extends DownloadInfo {
        private final String id;
        private final int totalSize;

        public AssetIndexInfo(String sha1, int size, String url, String id, int totalSize) {
            super(sha1, size, url);
            this.id = id;
            this.totalSize = totalSize;
        }

        public String getId() {
            return id;
        }

        public int getTotalSize() {
            return totalSize;
        }
    }
}
