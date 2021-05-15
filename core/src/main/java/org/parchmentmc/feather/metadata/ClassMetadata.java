package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.parchmentmc.feather.named.Named;

import java.util.List;

/**
 * Represents the metadata that is extracted during static analysis from a given class.
 */
public interface ClassMetadata extends SecuredObject, ClassOwnedObject, NamedObject
{
    /**
     * The name of the super class.
     *
     * @return The name holder of the super class.
     */
    @NonNull
    Named getSuperName();

    /**
     * A list of all names of interfaces that this class implements.
     *
     * @return The name holders of all implemented interfaces.
     */
    @NonNull
    List<Named> getInterfaceNames();

    /**
     * A list of all methods that reside in the current class.
     *
     * @return All methods of the current class.
     */
    @NonNull
    List<MethodMetadata> getMethods();

    /**
     * A list of all fields that reside in the current class.
     *
     * @return All fields of the current class.
     */
    @NonNull
    List<FieldMetadata> getFields();

    /**
     * A list of all inner classes that reside in the current class.
     *
     * @return All inner class of the current class.
     */
    @NonNull
    List<ClassMetadata> getInnerClasses();
}
