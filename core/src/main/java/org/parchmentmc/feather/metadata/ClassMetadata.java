package org.parchmentmc.feather.metadata;

import java.util.List;
import java.util.Map;

/**
 * Represents the metadata that is extracted during static analysis from a given class.
 */
public interface ClassMetadata
{
    /**
     * The obfuscated name of a given class.
     *
     * @return The obfuscated name.
     */
    String getObfuscatedName();

    /**
     * The mojang maps name of a given class.
     *
     * @return The mojmap name of a given class
     */
    String getMojMapName();

    /**
     * The obfuscated super name of a given class.
     *
     * @return The obfuscated name of the super class.
     */
    String getObfuscatedSuperName();

    /**
     * A list of all obfuscated names of interfaces that this class implements.
     *
     * @return The obfuscated names of all implemented interfaces.
     */
    List<String> getObfuscatedInterfaceNames();

    /**
     * The access specification in ASM bit field format.
     *
     * @return The access specification.
     */
    Integer getAccessSpecification();

    /**
     * A list of all methods that reside in the current class.
     *
     * @return All methods of the current class.
     */
    List<MethodMetadata> getMethods();

    /**
     * A list of all fields that reside in the current class.
     *
     * @return All fields of the current class.
     */
    List<FieldMetadata> getFields();
}
