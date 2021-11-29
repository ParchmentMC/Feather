package org.parchmentmc.feather.metadata;

import org.parchmentmc.feather.util.HasImmutable;

/**
 * Represents a single record in a record class.
 */
public interface RecordMetadata extends OwnedByClass, HasImmutable<RecordMetadata>
{

    /**
     * The field target of the record.
     *
     * @return The field
     */
    Reference getField();

    /**
     * The getter which returns the field from the record.
     *
     * @return The getter.
     */
    Reference getGetter();
}
