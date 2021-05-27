package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;

/**
 * Represents an object with an actual name in at least one mapping scheme.
 */
public interface WithName
{
    /**
     * The name of the class.
     *
     * @return The name holder of this class.
     */
    @NonNull
    Named getName();
}
