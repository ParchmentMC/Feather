package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;

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
}
