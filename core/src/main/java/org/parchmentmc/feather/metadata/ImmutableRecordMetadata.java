package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;

final class ImmutableRecordMetadata implements RecordMetadata {
    private final Named owner;
    private final Reference field;
    private final Reference getter;

    public ImmutableRecordMetadata(final Named owner, final Reference field, final Reference getter) {
        this.owner = owner;
        this.field = field;
        this.getter = getter;
    }

    @Override
    public @NonNull Named getOwner() {
        return owner;
    }

    @Override
    public Reference getField() {
        return field;
    }

    @Override
    public Reference getGetter() {
        return getter;
    }

    @Override
    public @NonNull RecordMetadata toImmutable() {
        return this;
    }
}
