package org.parchmentmc.feather.metadata;

import org.parchmentmc.feather.util.HasImmutable;

/**
 * Represents the metadata of a given field.
 */
public interface FieldMetadata extends OwnedByClass, WithName, WithType, WithSecurity, HasImmutable<FieldMetadata> {
}
