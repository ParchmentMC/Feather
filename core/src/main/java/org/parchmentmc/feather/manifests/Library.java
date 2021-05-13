package org.parchmentmc.feather.manifests;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * A library used by a game version.
 */
public class Library implements Serializable {
    private final String name;
    @Nullable
    private final ExtractInfo extract;
    private final LibraryDownloads downloads;
    @Nullable
    private final NativesInfo natives;
    private final List<Rule> rules;

    public Library(String name, @Nullable ExtractInfo extract, LibraryDownloads downloads, @Nullable NativesInfo natives,
                   List<Rule> rules) {
        this.name = name;
        this.extract = extract;
        this.downloads = downloads;
        this.natives = natives;
        this.rules = ImmutableList.copyOf(rules);
    }

    /**
     * Returns the name of the library.
     *
     * <p>This is formatted similarly to an Apache Maven dependency, but only having three groups:
     * <tt><em>group</em>:<em>name</em>:<em>version</em></tt></p>
     *
     * @return the library's name
     */
    public String getName() {
        return name;
    }

    /**
     * Return the extraction information, or {@code null} if the library will not be extracted.
     *
     * @return the extract information, or {@code null} if not to be extracted
     */
    @Nullable
    public ExtractInfo getExtract() {
        return extract;
    }

    /**
     * Returns the downloads information for this library.
     *
     * @return the download information
     */
    public LibraryDownloads getDownloads() {
        return downloads;
    }

    /**
     * Returns the nformation for the natives contained in this library, or {@code null} if no natives are contained.
     *
     * @return the natives information, or {@code null} if there are no natives
     */
    @Nullable
    public NativesInfo getNatives() {
        return natives;
    }

    /**
     * Returns the immutable list of rules for this library.
     *
     * <p>If there are no rules specified for a library, it will default to allowing the library. If there is at least one
     * rule specified, then the library is denied unless allowed by a rule.</p>
     *
     * @return the immutable list of rules
     */
    public List<Rule> getRules() {
        return rules;
    }

    /**
     * The information for the file downloads of the library.
     */
    public static class LibraryDownloads implements Serializable {
        @Nullable
        private final ArtifactDownload artifact;
        @Nullable
        private final Map<String, ArtifactDownload> classifiers;

        public LibraryDownloads(@Nullable ArtifactDownload artifact, @Nullable Map<String, ArtifactDownload> classifiers) {
            this.artifact = artifact;
            this.classifiers = classifiers != null ? ImmutableMap.copyOf(classifiers) : null;
        }

        /**
         * Returns the download info for a single artifact file, or {@code null} if not specified.
         *
         * <p>If this is {@code null}, the {@link #classifiers} map usually has artifacts specified, and vice-versa.</p>
         *
         * @return the download info for a single artifact, or {@code null} if there are multiple artifacts
         * @see #getClassifiers()
         */
        @Nullable
        public ArtifactDownload getArtifact() {
            return artifact;
        }

        /**
         * Returns an immutable map of classifiers to their respective artifact download information.
         *
         * <p>If this is {@code null}, the {@link #artifact} is usually specified, and vice-versa.</p>
         *
         * @return an immutable map of classifiers to artifact download info, or {@code null} if there are no classifiers
         */
        @Nullable
        public Map<String, ArtifactDownload> getClassifiers() {
            return classifiers;
        }
    }

    /**
     * The download information for a file for the library.
     */
    public static class ArtifactDownload extends VersionManifest.DownloadInfo {
        public String path;

        public ArtifactDownload(String sha1, int size, String url, String path) {
            super(sha1, size, url);
            this.path = path;
        }

        /**
         * Returns a relative path for storing the downloaded file.
         *
         * <p>This is formatted similarly to how an Apache Maven artifact would be stored on disk:
         * <tt>(<em>group...</em>/)<em>name</em>/<em>version</em>/<em>name</em>-<em>version</em>[-<em>classifier</em>].<em>extension</em></tt>.</p>
         *
         * @return a relative storage path
         */
        public String getPath() {
            return path;
        }
    }

    /**
     * Information used when extracting a library.
     */
    public static class ExtractInfo implements Serializable {
        private final List<String> exclude;

        public ExtractInfo(List<String> exclude) {
            this.exclude = ImmutableList.copyOf(exclude);
        }

        /**
         * Returns an immutable list of paths within the library which is excluded from extraction.
         *
         * @return an immutable list of excluded paths
         */
        public List<String> getExclude() {
            return exclude;
        }
    }

    /**
     * Information for the natives contained within this library.
     */
    public static class NativesInfo implements Serializable {
        private final String linux;
        private final String osx;
        private final String windows;

        public NativesInfo(String linux, String osx, String windows) {
            this.linux = linux;
            this.osx = osx;
            this.windows = windows;
        }

        /**
         * Returns the classifier key that contains the natives for the Linux operating system.
         *
         * <p>Note that this classifier key may not have an entry in the library's downloads.</p>
         *
         * @return the artifact classifier for the Linux natives
         * @see LibraryDownloads#classifiers
         */
        public String getLinux() {
            return linux;
        }

        /**
         * Returns the classifier key that contains the natives for the OSX operating system.
         *
         * <p>Note that this classifier key may not have an entry in the library's downloads.</p>
         *
         * @return the artifact classifier for the OSX natives
         * @see LibraryDownloads#classifiers
         */
        public String getOSX() {
            return osx;
        }

        /**
         * Returns the classifier key that contains the natives for the Windows operating system.
         *
         * <p>Note that this classifier key may not have an entry in the library's downloads.</p>
         *
         * @return the artifact classifier for the Windows natives
         * @see LibraryDownloads#classifiers
         */
        public String getWindows() {
            return windows;
        }
    }

    /**
     * A rule, used to add or remove libraries depending on conditions.
     *
     * <p>The action of the rule is applied if and only if the conditions of the rule are satisfied. If the rule
     * has no conditions specified, then it will always apply.</p>
     *
     * <p>If multiple rules are applied against a library, only the last one that has its conditions satisfied
     * will take effect.</p>
     */
    public static class Rule implements Serializable {
        private final String action;
        @Nullable
        private final OSCondition os;

        public Rule(String action, @Nullable OSCondition os) {
            this.action = action;
            this.os = os;
        }

        /**
         * Returns he action taken for the library, if the rule's conditions are satisfied.
         *
         * <p>Two values have been seen so far: </p>
         * <dl>
         *     <dt><tt><strong>allow</strong></tt></dt>
         *     <dd>Use this library for the game.</dd>
         *     <dt><tt><strong>deny</strong></tt></dt>
         *     <dd>Do not use this library for the game.</dd>
         * </dl>
         *
         * @return the action for this rule
         */
        public String getAction() {
            return action;
        }

        /**
         * Returns the OS condition for this rule, or {@code null} if this condition is not specified.
         *
         * @return the OS condition
         */
        @Nullable
        public OSCondition getOS() {
            return os;
        }

        /**
         * The rule condition matching against an OS.
         */
        public static class OSCondition implements Serializable {
            private final String name;
            private final String version;

            public OSCondition(String name, String version) {
                this.name = name;
                this.version = version;
            }

            /**
             * Returns the name of the OS that this rule matches against.
             *
             * @return the OS name to match against
             */
            public String getName() {
                return name;
            }

            /**
             * A regex pattern which matches against the version of the OS.
             *
             * @return the regex pattern for the OS version
             * @see java.util.regex.Pattern
             */
            public String getVersion() {
                return version;
            }
        }
    }
}
