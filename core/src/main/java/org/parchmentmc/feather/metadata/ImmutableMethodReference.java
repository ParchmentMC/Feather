package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;

import java.util.Objects;

public class ImmutableMethodReference implements MethodReference
{
    protected final Named owner;
    protected final Named name;
    protected final Named descriptor;
    protected final Named signature;

    public ImmutableMethodReference(
      final Named owner, final Named name, final Named descriptor, final Named signature)
    {
        this.owner = owner;
        this.name = name;
        this.descriptor = descriptor;
        this.signature = signature;
    }

    public @NonNull Named getOwner()
    {
        return owner;
    }

    public @NonNull Named getName()
    {
        return name;
    }

    public @NonNull Named getDescriptor()
    {
        return descriptor;
    }

    public @NonNull Named getSignature()
    {
        return signature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodReference)) return false;
        MethodReference that = (MethodReference) o;
        return Objects.equals(getOwner(), that.getOwner())
                && getName().equals(that.getName())
                && getDescriptor().equals(that.getDescriptor())
                && Objects.equals(getSignature(), that.getSignature());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOwner(), getName(), getDescriptor(), getSignature());
    }
}
