package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;

import java.util.List;

public final class MethodMetadataBuilder implements MethodMetadata
{
    private Named                 owner;
    private boolean               lambda;
    private MethodReference       bouncingTarget;
    private List<MethodReference> overrides;
    private Named                 name;
    private int                   securitySpecification;
    private Named                 descriptor;
    private Named                 signature;

    private MethodMetadataBuilder() {}

    public static MethodMetadataBuilder anImmutableMethodMetadata() { return new MethodMetadataBuilder(); }

    public MethodMetadataBuilder withOwner(Named owner)
    {
        this.owner = owner;
        return this;
    }

    public MethodMetadataBuilder withLambda(boolean lambda)
    {
        this.lambda = lambda;
        return this;
    }

    public MethodMetadataBuilder withBouncingTarget(MethodReference bouncingTarget)
    {
        this.bouncingTarget = bouncingTarget;
        return this;
    }

    public MethodMetadataBuilder withOverrides(List<MethodReference> overrides)
    {
        this.overrides = overrides;
        return this;
    }

    public MethodMetadataBuilder withName(Named name)
    {
        this.name = name;
        return this;
    }

    public MethodMetadataBuilder withSecuritySpecification(int securitySpecification)
    {
        this.securitySpecification = securitySpecification;
        return this;
    }

    public MethodMetadataBuilder withDescriptor(Named descriptor)
    {
        this.descriptor = descriptor;
        return this;
    }

    public MethodMetadataBuilder withSignature(Named signature)
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
    public boolean isLambda()
    {
        return lambda;
    }

    @Override
    public MethodReference getBouncingTarget()
    {
        return bouncingTarget;
    }

    @Override
    public List<MethodReference> getOverrides()
    {
        return overrides;
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

    public ImmutableMethodMetadata build() { return new ImmutableMethodMetadata(owner, lambda, bouncingTarget, overrides, name, securitySpecification, descriptor, signature); }

}
