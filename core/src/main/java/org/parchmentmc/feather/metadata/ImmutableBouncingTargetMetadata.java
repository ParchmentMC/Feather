package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

final class ImmutableBouncingTargetMetadata implements BouncingTargetMetadata
{
    private final Reference target;
    private final Reference owner;

    public ImmutableBouncingTargetMetadata(final Reference target, final Reference owner) {
        this.target = target;
        this.owner = owner;
    }

    @Override
    public Optional<Reference> getTarget()
    {
        return Optional.ofNullable(target);
    }

    @Override
    public Optional<Reference> getOwner()
    {
        return Optional.ofNullable(owner);
    }

    @Override
    public @NonNull BouncingTargetMetadata toImmutable()
    {
        return this;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof ImmutableBouncingTargetMetadata))
        {
            return false;
        }

        final ImmutableBouncingTargetMetadata that = (ImmutableBouncingTargetMetadata) o;

        if (getTarget().isPresent() ? !getTarget().equals(that.getTarget()) : that.getTarget().isPresent())
        {
            return false;
        }
        return getOwner().isPresent() ? getOwner().equals(that.getOwner()) : !that.getOwner().isPresent();
    }

    @Override
    public int hashCode()
    {
        int result = getTarget().isPresent() ? getTarget().hashCode() : 0;
        result = 31 * result + (getOwner().isPresent() ? getOwner().hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "ImmutableBouncingTargetMetadata{" +
                 "target=" + target +
                 ", owner=" + owner +
                 '}';
    }
}
