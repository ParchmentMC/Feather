package org.parchmentmc.feather.metadata;

import com.google.common.collect.Sets;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.named.NamedBuilder;
import org.parchmentmc.feather.util.AccessFlag;
import org.parchmentmc.feather.util.CollectorUtils;

import java.util.*;
import java.util.function.Function;

public final class MethodMetadataBuilder implements MethodMetadata {
    private Named owner = Named.empty();
    private boolean lambda = false;
    private BouncingTargetMetadata bouncingTarget = null;
    private LinkedHashSet<Reference> overrides = Sets.newLinkedHashSet();
    private Named name = Named.empty();
    private int securitySpecification = 0;
    private Named descriptor = Named.empty();
    private Named signature = Named.empty();
    private int startLine = 0;
    private int endLine = 0;
    private Reference parent = null;

    private MethodMetadataBuilder() {
    }

    public static MethodMetadataBuilder create(final MethodMetadata target) {
        if (target == null)
            return create();

        return create()
                .withOwner(target.getOwner())
                .withLambda(target.isLambda())
                .withBouncingTarget(target.getBouncingTarget().orElse(null))
                .withParent(target.getParent().orElse(null))
                .withOverrides(target.getOverrides())
                .withName(target.getName())
                .withSecuritySpecification(target.getSecuritySpecification())
                .withDescriptor(target.getDescriptor())
                .withSignature(target.getSignature())
                .withStartLine(target.getStartLine().orElse(0))
                .withEndLine(target.getEndLine().orElse(0));
    }

    public static MethodMetadataBuilder create() {
        return new MethodMetadataBuilder();
    }

    public MethodMetadataBuilder withEndLine(final int endLine) {
        this.endLine = endLine;
        return this;
    }

    public MethodMetadataBuilder withStartLine(final int startLine) {
        this.startLine = startLine;
        return this;
    }

    public MethodMetadataBuilder withSignature(Named signature) {
        this.signature = signature;
        return this;
    }

    public MethodMetadataBuilder withDescriptor(Named descriptor) {
        this.descriptor = descriptor;
        return this;
    }

    public MethodMetadataBuilder withSecuritySpecification(int securitySpecification) {
        this.securitySpecification = securitySpecification;
        return this;
    }

    public MethodMetadataBuilder withName(Named name) {
        this.name = name;
        return this;
    }

    public MethodMetadataBuilder withOverrides(LinkedHashSet<Reference> overrides) {
        this.overrides = overrides;
        return this;
    }

    public MethodMetadataBuilder withParent(final Reference parent) {
        this.parent = parent;
        return this;
    }

    public MethodMetadataBuilder withBouncingTarget(BouncingTargetMetadata bouncingTarget) {
        this.bouncingTarget = bouncingTarget;
        return this;
    }

    public MethodMetadataBuilder withLambda(boolean lambda) {
        this.lambda = lambda;
        return this;
    }

    public MethodMetadataBuilder withOwner(Named owner) {
        this.owner = owner;
        return this;
    }

    public MethodMetadataBuilder merge(final MethodMetadata source, final String schema) {
        if (source == null)
            return this;

        this.owner = NamedBuilder.create(this.owner)
                .merge(source.getOwner())
                .build();
        this.lambda = this.lambda || source.isLambda();
        this.bouncingTarget = BouncingTargetMetadataBuilder.create(this.bouncingTarget)
                .merge(source.getBouncingTarget().orElse(null))
                .build();

        final Map<Reference, Reference> schemadLocalOverrides =
                this.overrides.stream().collect(CollectorUtils.toLinkedMap(
                        mr -> ReferenceBuilder.create()
                                .withName(NamedBuilder.create()
                                        .with(schema, mr.getName().getName(schema).orElse(""))
                                )
                                .withOwner(NamedBuilder.create()
                                        .with(schema, mr.getOwner().getName(schema).orElse(""))
                                )
                                .withDescriptor(NamedBuilder.create()
                                        .with(schema, mr.getDescriptor().getName(schema).orElse(""))
                                )
                                .withSignature(NamedBuilder.create()
                                        .with(schema, mr.getSignature().getName(schema).orElse(""))
                                )
                                .build(),
                        Function.identity(),
                        (t, i) -> t
                ));

        final Map<Reference, Reference> schemadSoureOverrides =
                source.getOverrides().stream().collect(
                        CollectorUtils.toLinkedMap(
                                mr -> ReferenceBuilder.create()
                                        .withName(NamedBuilder.create()
                                                .with(schema, mr.getName().getName(schema).orElse(""))
                                        )
                                        .withOwner(NamedBuilder.create()
                                                .with(schema, mr.getOwner().getName(schema).orElse(""))
                                        )
                                        .withDescriptor(NamedBuilder.create()
                                                .with(schema, mr.getDescriptor().getName(schema).orElse(""))
                                        )
                                        .withSignature(NamedBuilder.create()
                                                .with(schema, mr.getSignature().getName(schema).orElse(""))
                                        )
                                        .build(),
                                Function.identity(),
                                (t, i) -> t
                        ));

        this.overrides = new LinkedHashSet<>();
        for (final Reference keyReference : schemadLocalOverrides.keySet()) {
            if (!schemadSoureOverrides.containsKey(keyReference)) {
                this.overrides.add(schemadLocalOverrides.get(keyReference));
            } else {
                this.overrides.add(
                        ReferenceBuilder.create(schemadLocalOverrides.get(keyReference))
                                .merge(schemadSoureOverrides.get(keyReference))
                                .build()
                );
            }
        }
        schemadSoureOverrides.keySet().stream()
                .filter(mr -> !schemadLocalOverrides.containsKey(mr))
                .forEach(mr -> this.overrides.add(schemadSoureOverrides.get(mr)));

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

        this.startLine = source.getStartLine().orElse(this.startLine);
        this.endLine = source.getEndLine().orElse(this.endLine);

        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOwner(),
                isLambda(),
                getBouncingTarget(),
                getOverrides(),
                getName(),
                getSecuritySpecification(),
                getDescriptor(),
                getSignature(),
                getStartLine(),
                getEndLine());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MethodMetadataBuilder)) {
            return false;
        }
        final MethodMetadataBuilder builder = (MethodMetadataBuilder) o;
        return isLambda() == builder.isLambda()
                && getSecuritySpecification() == builder.getSecuritySpecification()
                && getStartLine() == builder.getStartLine()
                && getEndLine() == builder.getEndLine()
                && getOwner().equals(builder.getOwner())
                && getBouncingTarget().equals(builder.getBouncingTarget())
                && getOverrides().equals(builder.getOverrides())
                && getName().equals(builder.getName())
                && getDescriptor().equals(builder.getDescriptor())
                && getSignature().equals(builder.getSignature());
    }

    @Override
    public @NonNull Named getOwner() {
        return owner;
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
        if (startLine <= 0) {
            return Optional.empty();
        }

        return Optional.of(startLine);
    }

    @Override
    public @NonNull Optional<Integer> getEndLine() {
        if (endLine <= 0) {
            return Optional.empty();
        }

        return Optional.of(endLine);
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
    public @NonNull MethodMetadata toImmutable() {
        return build();
    }

    public MethodMetadata build() {
        return new ImmutableMethodMetadata(
                owner.toImmutable(),
                lambda,
                bouncingTarget == null ? null : bouncingTarget.toImmutable(),
                parent,
                overrides.stream().map(Reference::toImmutable).collect(CollectorUtils.toLinkedSet()),
                name.toImmutable(),
                securitySpecification,
                descriptor.toImmutable(),
                signature.toImmutable(),
                startLine,
                endLine
        );
    }
}
