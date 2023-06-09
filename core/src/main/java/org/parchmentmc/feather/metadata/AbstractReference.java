package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;

import java.util.Objects;

public class AbstractReference implements BaseReference {
    protected final Named owner;
    protected final Named name;
    protected final Named descriptor;
    protected final Named signature;

    public AbstractReference(final Named owner, final Named name, final Named descriptor, final Named signature) {
        this.owner = owner;
        this.name = name;
        this.descriptor = descriptor;
        this.signature = signature;
    }

    @Override
    public @NonNull Named getOwner() {
        return owner;
    }

    @Override
    public @NonNull Named getName() {
        return name;
    }

    @Override
    public @NonNull Named getDescriptor() {
        return descriptor;
    }

    @Override
    public @NonNull Named getSignature() {
        return signature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractReference)) {
            return false;
        }
        AbstractReference that = (AbstractReference) o;
        return Objects.equals(getOwner(), that.getOwner())
                && getName().equals(that.getName())
                && getDescriptor().equals(that.getDescriptor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOwner(), getName(), getDescriptor(), getSignature());
    }
}
