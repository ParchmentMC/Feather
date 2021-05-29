package org.parchmentmc.feather.metadata;

import org.parchmentmc.feather.util.HasImmutable;

/**
 * Represents a reference to a given method which can be converted into an immutable object.
 */
public interface MethodReference extends BaseMethodReference, HasImmutable<MethodReference> {
}
