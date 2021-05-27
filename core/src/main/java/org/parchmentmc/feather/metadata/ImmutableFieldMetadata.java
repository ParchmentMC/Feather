package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;

import java.util.Objects;

public class ImmutableFieldMetadata implements FieldMetadata {
    private final Named owner;
    private final Named name;
    private final int securitySpecification;
    private final Named descriptor;
    private final Named signature;

    public ImmutableFieldMetadata(final Named owner, final Named name, final int securitySpecification, final Named descriptor, final Named signature) {
        this.owner = owner;
        this.name = name;
        this.securitySpecification = securitySpecification;
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
    public int getSecuritySpecification() {
        return securitySpecification;
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
        if (this == o) return true;
        if (!(o instanceof FieldMetadata)) return false;
        FieldMetadata that = (FieldMetadata) o;
        return getSecuritySpecification() == that.getSecuritySpecification()
                && Objects.equals(getOwner(), that.getOwner())
                && getName().equals(that.getName())
                && getDescriptor().equals(that.getDescriptor())
                && Objects.equals(getSignature(), that.getSignature());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOwner(), getName(), getSecuritySpecification(), getDescriptor(), getSignature());
    }

    @Override
    public String toString()
    {
        return "ImmutableFieldMetadata{" +
                 "owner=" + owner +
                 ", name=" + name +
                 ", securitySpecification=" + securitySpecification +
                 ", descriptor=" + descriptor +
                 ", signature=" + signature +
                 '}';
    }

    @Override
    public @NonNull FieldMetadata toImmutable()
    {
        return this;
    }
}
