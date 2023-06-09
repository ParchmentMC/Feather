package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;

final class ImmutableReference extends AbstractReference implements Reference {

    public ImmutableReference(final Named owner, final Named name, final Named descriptor, final Named signature) {
        super(owner, name, descriptor, signature);
    }

    @Override
    public String toString() {
        return "ImmutableMethodReference{" +
                "owner=" + owner +
                ", name=" + name +
                ", descriptor=" + descriptor +
                ", signature=" + signature +
                '}';
    }

    @Override
    public @NonNull Reference toImmutable() {
        return this;
    }
}
