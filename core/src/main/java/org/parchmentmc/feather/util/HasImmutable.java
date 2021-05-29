package org.parchmentmc.feather.util;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an object that can be converted into an immutable.
 *
 * @param <I> The type that implements this interface.
 */
public interface HasImmutable<I extends HasImmutable<I>> {

    /**
     * Creates an immutable version of this object.
     *
     * @return The immutbale version of this object.
     */
    @NonNull
    I toImmutable();
}
