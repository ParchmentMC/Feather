package org.parchmentmc.feather.named;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.parchmentmc.feather.util.Constants;

import java.util.Map;
import java.util.Optional;

/**
 * Represents a named object that potentially has names in different mapping types (also known as schemas)
 */
public interface Named
{
    /**
     * Returns all known schemas and their names for this named object.
     *
     * @return All known schemas and their names.
     */
    Map<String, String> getNames();

    /**
     * Returns whether this named object has no names.
     *
     * @return if this object has no names
     */
    default boolean isEmpty() {
        return getNames().isEmpty();
    }

    /**
     * Looks up a name in the obfuscated schema ({@link Constants.Names#OBFUSCATED}).
     *
     * @return The obfuscated name.
     */
    default Optional<String> getObfuscatedName() {
        return getObfuscatedName(null);
    }

    /**
     * Looks up a name in the obfuscated schema ({@link Constants.Names#OBFUSCATED}).
     * If no obfuscated name exists the {@code orElse} value is returned.
     *
     * @param orElse The value that should be returned if this object does not have an obfuscated name.
     * @return The obfuscated name or the {@code orElse} value.
     */
    default Optional<String> getObfuscatedName(@Nullable String orElse) {
        return getName(Constants.Names.OBFUSCATED, orElse);
    }

    /**
     * Looks up a name in the mojang schema ({@link Constants.Names#MOJANG}).
     *
     * @return The mojang name.
     */
    default Optional<String> getMojangName() {
        return getMojangName(null);
    }

    /**
     * Looks up a name in the mojang schema ({@link Constants.Names#MOJANG}).
     * If no mojang name exists the {@code orElse} value is returned.
     *
     * @param orElse The value that should be returned if this object does not have a mojang name.
     * @return The mojang name or the {@code orElse} value.
     */
    default Optional<String> getMojangName(String orElse) {
        return getName(Constants.Names.MOJANG, orElse);
    }

    /**
     * Looks up a name with the given schema in this named object.
     *
     * @param scheme The schema for the name.
     * @return The name registered for the given schema.
     */
    default Optional<String> getName(String scheme) {
        return getName(scheme, null);
    }

    /**
     * Looks up a name with the given schema in this named object.
     * If no schema exists in this named object then the {@code orElse} value is returned.
     *
     * @param scheme The schema to look the name up for.
     * @param orElse The value returned when this named object does not contain a name for the given schema.
     * @return The name for the given schema or the {@code orElse} value.
     */
    default Optional<String> getName(String scheme, String orElse) {
        return Optional.ofNullable(getNames().getOrDefault(scheme, orElse));
    }
}
