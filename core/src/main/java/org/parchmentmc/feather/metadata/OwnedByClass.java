package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;

/**
 * Represents an object which resides in a class.
 */
public interface OwnedByClass {
    /**
     * The name holder of the class that this object resides in.
     *
     * @return The name of the class.
     */
    @NonNull
    Named getOwner();
}
