package org.parchmentmc.feather.metadata;

/**
 * Represents a reference to a given method.
 * Specifies the class the method is in, its name and its descriptor.
 *
 * All three key values, are specified in both a obfuscated and a mojang maps form.
 */
public interface MethodReferenceMetadata
{
    /**
     * The obfuscated name of the class that this method is part of.
     *
     * @return The obfuscated method name of the owner of the class.
     */
    String getObfuscatedOwnerName();

    /**
     * The obfuscated name of the method itself.
     *
     * @return The obfuscated name of the method.
     */
    String getObfuscatedName();

    /**
     * The obfuscated descriptor of the method.
     *
     * @return The obfuscated descriptor.
     */
    String getObfuscatedDescriptor();
}
