package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.ImmutableNamed;
import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.named.NamedBuilder;
import org.parchmentmc.feather.util.AccessFlag;

import java.util.EnumSet;
import java.util.Objects;

public final class FieldMetadataBuilder implements FieldMetadata {
    private Named owner = ImmutableNamed.empty();
    private Named name = ImmutableNamed.empty();
    private int securitySpecification = 0;
    private Named descriptor = ImmutableNamed.empty();
    private Named signature = ImmutableNamed.empty();

    private FieldMetadataBuilder() {
    }

    public static FieldMetadataBuilder create() {
        return new FieldMetadataBuilder();
    }

    public static FieldMetadataBuilder create(final FieldMetadata target) {
        return create()
                .withOwner(target.getOwner())
                .withName(target.getName())
                .withSecuritySpecification(target.getSecuritySpecification())
                .withDescriptor(target.getDescriptor())
                .withSignature(target.getSignature());
    }

    public FieldMetadataBuilder withOwner(Named owner) {
        this.owner = owner;
        return this;
    }

    public FieldMetadataBuilder withName(Named name) {
        this.name = name;
        return this;
    }

    public FieldMetadataBuilder withSecuritySpecification(int securitySpecification) {
        this.securitySpecification = securitySpecification;
        return this;
    }

    public FieldMetadataBuilder withDescriptor(Named descriptor) {
        this.descriptor = descriptor;
        return this;
    }

    public FieldMetadataBuilder withSignature(Named signature) {
        this.signature = signature;
        return this;
    }

    public FieldMetadataBuilder merge(final FieldMetadata source) {
        this.owner = NamedBuilder.create(this.owner)
                .merge(source.getOwner())
                .build();
        this.name = NamedBuilder.create(this.name)
                .merge(source.getName())
                .build();

        final EnumSet<AccessFlag> thisAccessFlags = AccessFlag.getAccessFlags(this.securitySpecification);
        final EnumSet<AccessFlag> sourceAccessFlags = AccessFlag.getAccessFlags(source.getSecuritySpecification());

        final EnumSet<AccessFlag> mergedFlags = EnumSet.noneOf(AccessFlag.class);
        mergedFlags.addAll(thisAccessFlags);
        mergedFlags.addAll(sourceAccessFlags);

        this.securitySpecification = AccessFlag.toSecuritySpecification(mergedFlags);

        this.descriptor = NamedBuilder.create(this.descriptor)
                .merge(source.getDescriptor())
                .build();
        this.signature = NamedBuilder.create(this.signature)
                .merge(source.getSignature())
                .build();

        return this;
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

    public FieldMetadata build() {
        return new ImmutableFieldMetadata(
                owner.toImmutable(),
                name.toImmutable(),
                securitySpecification,
                descriptor.toImmutable(),
                signature.toImmutable()
        );
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
    public @NonNull FieldMetadata toImmutable() {
        return build();
    }

}
