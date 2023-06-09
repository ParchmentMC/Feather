package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.named.NamedBuilder;

import java.util.Objects;

public final class ReferenceBuilder implements Reference {
    private Named owner = Named.empty();
    private Named name = Named.empty();
    private Named descriptor = Named.empty();
    private Named signature = Named.empty();

    private ReferenceBuilder() {
    }

    public static ReferenceBuilder create(final Reference methodReference) {
        if (methodReference == null) {
            return create();
        }

        return create()
                .withOwner(methodReference.getOwner())
                .withName(methodReference.getName())
                .withDescriptor(methodReference.getDescriptor())
                .withSignature(methodReference.getSignature());
    }

    public static ReferenceBuilder create() {
        return new ReferenceBuilder();
    }

    public ReferenceBuilder withSignature(Named signature) {
        this.signature = signature;
        return this;
    }

    public ReferenceBuilder withDescriptor(Named descriptor) {
        this.descriptor = descriptor;
        return this;
    }

    public ReferenceBuilder withName(Named name) {
        this.name = name;
        return this;
    }

    public ReferenceBuilder withOwner(Named owner) {
        this.owner = owner;
        return this;
    }

    public ReferenceBuilder merge(@Nullable final Reference source) {
        if (source == null) {
            return this;
        }

        this.owner = NamedBuilder.create(this.owner)
                .merge(source.getOwner())
                .build();
        this.name = NamedBuilder.create(this.name)
                .merge(source.getName())
                .build();
        this.descriptor = NamedBuilder.create(this.descriptor)
                .merge(source.getDescriptor())
                .build();
        this.signature = NamedBuilder.create(this.signature)
                .merge(source.getSignature())
                .build();

        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOwner(), getName(), getDescriptor(), getSignature());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reference)) {
            return false;
        }
        Reference that = (Reference) o;
        return Objects.equals(getOwner(), that.getOwner())
                && getName().equals(that.getName())
                && getDescriptor().equals(that.getDescriptor());
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
    public @NonNull Named getDescriptor() {
        return descriptor;
    }

    @Override
    public @NonNull Named getSignature() {
        return signature;
    }

    @Override
    public @NonNull Reference toImmutable() {
        return build();
    }

    public ImmutableReference build() {
        return new ImmutableReference(
                owner.toImmutable(),
                name.toImmutable(),
                descriptor.toImmutable(),
                signature.toImmutable()
        );
    }
}
