package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;

final class ImmutableMethodMetadata extends AbstractReference implements MethodMetadata {
    private final boolean lambda;
    private final BouncingTargetMetadata bouncingTarget;
    private final LinkedHashSet<Reference> overrides;
    private final int securitySpecification;
    private final int startLine;
    private final int endLine;
    private final Reference parent;

    public ImmutableMethodMetadata(
            final Named owner,
            final boolean lambda,
            final BouncingTargetMetadata bouncingTarget,
            final Reference parent, final LinkedHashSet<Reference> overrides,
            final Named name,
            final int securitySpecification,
            final Named descriptor,
            final Named signature,
            final int startLine,
            final int endLine) {
        super(owner, name, descriptor, signature);
        this.lambda = lambda;
        this.bouncingTarget = bouncingTarget;
        this.parent = parent;
        this.overrides = new LinkedHashSet<>(overrides);
        this.securitySpecification = securitySpecification;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    @Override
    public boolean isLambda() {
        return lambda;
    }

    @Override
    public Optional<BouncingTargetMetadata> getBouncingTarget() {
        return Optional.ofNullable(bouncingTarget);
    }

    @Override
    public Optional<Reference> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    @NonNull
    public LinkedHashSet<Reference> getOverrides() {
        return overrides;
    }

    @Override
    public @NonNull Optional<Integer> getStartLine() {
        if (startLine <= 0)
            return Optional.empty();

        return Optional.of(startLine);
    }

    @Override
    public @NonNull Optional<Integer> getEndLine() {
        if (endLine <= 0)
            return Optional.empty();

        return Optional.of(endLine);
    }

    @Override
    public int getSecuritySpecification() {
        return securitySpecification;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImmutableMethodMetadata)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final ImmutableMethodMetadata that = (ImmutableMethodMetadata) o;
        return isLambda() == that.isLambda() &&
                getSecuritySpecification() == that.getSecuritySpecification() &&
                getStartLine().equals(that.getStartLine()) &&
                getEndLine().equals(that.getEndLine()) &&
                Objects.equals(getBouncingTarget(), that.getBouncingTarget()) &&
                Objects.equals(getOverrides(), that.getOverrides());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isLambda(), getBouncingTarget(), getOverrides(), getSecuritySpecification(), getStartLine(), getEndLine());
    }

    @Override
    public String toString() {
        return "ImmutableMethodMetadata{" +
                "owner=" + owner +
                ", name=" + name +
                ", descriptor=" + descriptor +
                ", signature=" + signature +
                ", lambda=" + lambda +
                ", bouncingTarget=" + bouncingTarget +
                ", overrides=" + overrides +
                ", securitySpecification=" + securitySpecification +
                ", startLine=" + startLine +
                ", endLine=" + endLine +
                '}';
    }

    @Override
    public @NonNull MethodMetadata toImmutable() {
        return this;
    }
}
