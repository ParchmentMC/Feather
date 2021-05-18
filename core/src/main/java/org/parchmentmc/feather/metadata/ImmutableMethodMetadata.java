package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.parchmentmc.feather.named.Named;

import java.util.List;
import java.util.Objects;

public class ImmutableMethodMetadata extends ImmutableMethodReference implements MethodMetadata {
    private final boolean lambda;
    private final MethodReference bouncingTarget;
    private final List<MethodReference> overrides;
    private final int securitySpecification;

    public ImmutableMethodMetadata(
            final Named owner,
            final boolean lambda,
            final MethodReference bouncingTarget,
            final List<MethodReference> overrides,
            final Named name,
            final int securitySpecification,
            final Named descriptor, final Named signature) {
        super(owner, name, descriptor, signature);
        this.lambda = lambda;
        this.bouncingTarget = bouncingTarget;
        this.overrides = overrides;
        this.securitySpecification = securitySpecification;
    }

    @Override
    public boolean isLambda() {
        return lambda;
    }

    @Override
    @Nullable
    public MethodReference getBouncingTarget() {
        return bouncingTarget;
    }

    @Override
    public @NonNull List<MethodReference> getOverrides() {
        return overrides;
    }

    @Override
    public int getSecuritySpecification() {
        return securitySpecification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodMetadata)) return false;
        if (!super.equals(o)) return false;
        MethodMetadata that = (MethodMetadata) o;
        return isLambda() == that.isLambda()
                && getSecuritySpecification() == that.getSecuritySpecification()
                && Objects.equals(getBouncingTarget(), that.getBouncingTarget())
                && getOverrides().equals(that.getOverrides());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isLambda(), getBouncingTarget(), getOverrides(), getSecuritySpecification());
    }
}
