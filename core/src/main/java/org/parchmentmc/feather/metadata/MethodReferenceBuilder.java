package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.parchmentmc.feather.named.ImmutableNamed;
import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.named.NamedBuilder;

import java.util.Objects;

public final class MethodReferenceBuilder implements MethodReference {
    protected Named owner = ImmutableNamed.empty();
    protected Named name = ImmutableNamed.empty();
    protected Named descriptor = ImmutableNamed.empty();
    protected Named signature = ImmutableNamed.empty();

    private MethodReferenceBuilder() {
    }

    public static MethodReferenceBuilder create() {
        return new MethodReferenceBuilder();
    }

    public static MethodReferenceBuilder create(final MethodReference methodReference) {
        return create()
                .withOwner(methodReference.getOwner())
                .withName(methodReference.getName())
                .withDescriptor(methodReference.getDescriptor())
                .withSignature(methodReference.getSignature());
    }

    public MethodReferenceBuilder withOwner(Named owner) {
        this.owner = owner;
        return this;
    }

    public MethodReferenceBuilder withName(Named name) {
        this.name = name;
        return this;
    }

    public MethodReferenceBuilder withDescriptor(Named descriptor) {
        this.descriptor = descriptor;
        return this;
    }

    public MethodReferenceBuilder withSignature(Named signature) {
        this.signature = signature;
        return this;
    }

    public MethodReferenceBuilder merge(@Nullable final MethodReference source) {
        if (source == null)
            return this;

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

    public ImmutableMethodReference build() {
        return new ImmutableMethodReference(
                owner.toImmutable(),
                name.toImmutable(),
                descriptor.toImmutable(),
                signature.toImmutable()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodReference)) return false;
        MethodReference that = (MethodReference) o;
        return Objects.equals(getOwner(), that.getOwner())
                && getName().equals(that.getName())
                && getDescriptor().equals(that.getDescriptor())
                && Objects.equals(getSignature(), that.getSignature());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOwner(), getName(), getDescriptor(), getSignature());
    }

    @Override
    public @NonNull MethodReference toImmutable() {
        return build();
    }
}
