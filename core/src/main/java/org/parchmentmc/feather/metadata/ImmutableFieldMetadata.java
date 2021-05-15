package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;

public class ImmutableFieldMetadata implements FieldMetadata
{
    private final Named owner;
    private final Named name;
    private final int   securitySpecification;
    private final Named descriptor;
    private final Named signature;

    public ImmutableFieldMetadata(final Named owner, final Named name, final int securitySpecification, final Named descriptor, final Named signature)
    {
        this.owner = owner;
        this.name = name;
        this.securitySpecification = securitySpecification;
        this.descriptor = descriptor;
        this.signature = signature;
    }

    @Override
    public @NonNull Named getOwner()
    {
        return owner;
    }

    @Override
    public @NonNull Named getName()
    {
        return name;
    }

    @Override
    public int getSecuritySpecification()
    {
        return securitySpecification;
    }

    @Override
    public @NonNull Named getDescriptor()
    {
        return descriptor;
    }

    @Override
    public @NonNull Named getSignature()
    {
        return signature;
    }
}
