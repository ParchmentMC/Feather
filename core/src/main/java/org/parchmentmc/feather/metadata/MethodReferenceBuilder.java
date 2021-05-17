package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.ImmutableNamed;
import org.parchmentmc.feather.named.Named;

public final class MethodReferenceBuilder implements MethodReference
{
    protected Named owner = new ImmutableNamed();
    protected Named name = new ImmutableNamed();
    protected Named descriptor = new ImmutableNamed();
    protected Named signature = new ImmutableNamed();

    private MethodReferenceBuilder() {}

    public static MethodReferenceBuilder create() { return new MethodReferenceBuilder(); }

    public MethodReferenceBuilder withOwner(Named owner)
    {
        this.owner = owner;
        return this;
    }

    public MethodReferenceBuilder withName(Named name)
    {
        this.name = name;
        return this;
    }

    public MethodReferenceBuilder withDescriptor(Named descriptor)
    {
        this.descriptor = descriptor;
        return this;
    }

    public MethodReferenceBuilder withSignature(Named signature)
    {
        this.signature = signature;
        return this;
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
    public @NonNull Named getDescriptor()
    {
        return descriptor;
    }

    @Override
    public @NonNull Named getSignature()
    {
        return signature;
    }

    public ImmutableMethodReference build() { return new ImmutableMethodReference(owner, name, descriptor, signature); }
}
