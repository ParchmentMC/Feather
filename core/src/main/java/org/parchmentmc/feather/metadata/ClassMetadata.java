package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.util.HasImmutable;

import java.util.LinkedHashSet;

/**
 * Represents the metadata that is extracted during static analysis from a given class.
 */
public interface ClassMetadata extends WithSecurity, OwnedByClass, WithName, HasImmutable<ClassMetadata> {
    /**
     * The name of the super class.
     *
     * @return The name holder of the super class.
     */
    @NonNull
    Named getSuperName();

    /**
     * A Set of all names of interfaces that this class implements.
     *
     * @return The name holders of all implemented interfaces.
     */
    @NonNull
    LinkedHashSet<Named> getInterfaces();

    /**
     * A Set of all methods that reside in the current class.
     *
     * @return All methods of the current class.
     */
    @NonNull
    LinkedHashSet<MethodMetadata> getMethods();

    /**
     * A Set of all fields that reside in the current class.
     *
     * @return All fields of the current class.
     */
    @NonNull
    LinkedHashSet<FieldMetadata> getFields();

    /**
     * A set of all records of this class, if the class is a record.
     *
     * @return All records of the current class.
     */
    @NonNull
    LinkedHashSet<RecordMetadata> getRecords();

    /**
     * A Set of all inner classes that reside in the current class.
     *
     * @return All inner class of the current class.
     */
    @NonNull
    LinkedHashSet<ClassMetadata> getInnerClasses();

    /**
     * The generic signature of the class if present.
     *
     * @return The generic signature.
     */
    @NonNull
    Named getSignature();

    /**
     * Indicates if the class is a record.
     *
     * @return True if the class is a record, false otherwise.
     */
    boolean isRecord();
}
