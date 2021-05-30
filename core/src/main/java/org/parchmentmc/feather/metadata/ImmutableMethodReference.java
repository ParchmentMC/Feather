package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;

final class ImmutableMethodReference extends AbstractMethodReference implements MethodReference {

    public ImmutableMethodReference(final Named owner, final Named name, final Named descriptor, final Named signature) {
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
    public @NonNull MethodReference toImmutable() {
        return this;
    }
}
