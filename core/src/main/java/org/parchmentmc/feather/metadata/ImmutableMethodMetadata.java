package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;

import java.util.List;

public class ImmutableMethodMetadata implements MethodMetadata
{
    private final Named                 owner;
    private final boolean               lambda;
    private final MethodReference       bouncingTarget;
    private final List<MethodReference> overrides;
    private final Named                 name;
    private final int                   securitySpecification;
    private final Named                 descriptor;
    private final Named                 signature;

    public ImmutableMethodMetadata(
      final Named owner,
      final boolean lambda,
      final MethodReference bouncingTarget,
      final List<MethodReference> overrides,
      final Named name,
      final int securitySpecification,
      final Named descriptor, final Named signature)
    {
        this.owner = owner;
        this.lambda = lambda;
        this.bouncingTarget = bouncingTarget;
        this.overrides = overrides;
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
}
