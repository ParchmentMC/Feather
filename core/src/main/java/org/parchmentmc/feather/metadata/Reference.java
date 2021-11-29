package org.parchmentmc.feather.metadata;

import org.parchmentmc.feather.util.HasImmutable;

/**
 * Represents a reference to a given method or field which can be converted into an immutable object.
 */
public interface Reference extends BaseReference, HasImmutable<Reference> {
}
