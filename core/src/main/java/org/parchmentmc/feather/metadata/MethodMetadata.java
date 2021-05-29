package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.parchmentmc.feather.util.HasImmutable;

import java.util.LinkedHashSet;
import java.util.Optional;

/**
 * Represents the metadata of a given method.
 */
public interface MethodMetadata extends BaseMethodReference, WithSecurity, HasImmutable<MethodMetadata> {
    /**
     * Indicates if this method is a lambda method.
     *
     * @return True when this is a lambda.
     */
    boolean isLambda();

    /**
     * The target of this method if it is a bouncer.
     * Is null if this method is not a bouncer.
     *
     * @return The bouncer target.
     */
    @Nullable
    MethodReference getBouncingTarget();

    /**
     * The methods this method overrides in super classes that the owner either extends or inherits from.
     *
     * @return The methods this method overrides.
     */
    @NonNull
    LinkedHashSet<MethodReference> getOverrides();

    /**
     * Indicates the line where this method starts in the obfuscated none patched none updated none remapped jar.
     *
     * @return The start line of the method in the primary jar.
     */
    @NonNull
    Optional<Integer> getStartLine();

    /**
     * Indicates the line where this method ends in the obfuscated none patched none updated none remapped jar.
     *
     * @return The end line of the method in the primary jar.
     */
    @NonNull
    Optional<Integer> getEndLine();
}
