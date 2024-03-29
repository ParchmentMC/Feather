package org.parchmentmc.feather.metadata;

import com.google.common.collect.Sets;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.named.NamedBuilder;
import org.parchmentmc.feather.util.AccessFlag;
import org.parchmentmc.feather.util.CollectorUtils;

import java.util.*;
import java.util.function.Function;

public final class ClassMetadataBuilder implements ClassMetadata {
    private Named superName = Named.empty();
    private LinkedHashSet<Named> interfaces = Sets.newLinkedHashSet();
    private LinkedHashSet<MethodMetadata> methods = Sets.newLinkedHashSet();
    private LinkedHashSet<FieldMetadata> fields = Sets.newLinkedHashSet();
    private LinkedHashSet<RecordMetadata> records = Sets.newLinkedHashSet();
    private LinkedHashSet<ClassMetadata> innerClasses = Sets.newLinkedHashSet();
    private Named owner = Named.empty();
    private Named name = Named.empty();
    private int securitySpecifications = 0;
    private Named signature = Named.empty();
    private boolean isRecord = false;

    private ClassMetadataBuilder() {
    }

    public static ClassMetadataBuilder create() {
        return new ClassMetadataBuilder();
    }

    public static ClassMetadataBuilder create(final ClassMetadata classMetadata) {
        if (classMetadata == null) {
            return create();
        }

        return create()
                .withSuperName(classMetadata.getSuperName())
                .withInterfaces(classMetadata.getInterfaces())
                .withMethods(classMetadata.getMethods())
                .withFields(classMetadata.getFields())
                .withInnerClasses(classMetadata.getInnerClasses())
                .withOwner(classMetadata.getOwner())
                .withName(classMetadata.getName())
                .withSecuritySpecifications(classMetadata.getSecuritySpecification())
                .withRecords(classMetadata.getRecords())
                .withIsRecord(classMetadata.isRecord());
    }

    public ClassMetadataBuilder withSuperName(Named superName) {
        this.superName = superName;
        return this;
    }

    public ClassMetadataBuilder withInterfaces(Set<Named> interfaces) {
        this.interfaces = new LinkedHashSet<>(interfaces);
        return this;
    }

    public ClassMetadataBuilder withMethods(Set<MethodMetadata> methods) {
        this.methods = new LinkedHashSet<>(methods);
        return this;
    }

    public ClassMetadataBuilder addMethod(final MethodMetadata method) {
        this.methods.add(method);
        return this;
    }

    public ClassMetadataBuilder withFields(Set<FieldMetadata> fields) {
        this.fields = new LinkedHashSet<>(fields);
        return this;
    }

    public ClassMetadataBuilder addField(final FieldMetadata field) {
        this.fields.add(field);
        return this;
    }

    public ClassMetadataBuilder withRecords(Set<RecordMetadata> records) {
        this.records = new LinkedHashSet<>(records);
        this.isRecord = !records.isEmpty();
        return this;
    }

    public ClassMetadataBuilder addRecord(final RecordMetadata record) {
        this.records.add(record);
        this.isRecord = true;
        return this;
    }

    public ClassMetadataBuilder withInnerClasses(Set<ClassMetadata> innerClasses) {
        this.innerClasses = new LinkedHashSet<>(innerClasses);
        return this;
    }

    public ClassMetadataBuilder addInnerClass(final ClassMetadata classMetadata) {
        this.innerClasses.add(classMetadata);
        return this;
    }

    public ClassMetadataBuilder withOwner(Named owner) {
        this.owner = owner;
        return this;
    }

    public ClassMetadataBuilder withName(Named name) {
        this.name = name;
        return this;
    }

    public ClassMetadataBuilder withSecuritySpecifications(int securitySpecifications) {
        this.securitySpecifications = securitySpecifications;
        return this;
    }

    public ClassMetadataBuilder withSignature(final Named signature) {
        this.signature = signature;
        return this;
    }

    public ClassMetadataBuilder withIsRecord(final boolean isRecord) {
        this.isRecord = isRecord;
        return this;
    }

    public ClassMetadataBuilder merge(final ClassMetadata source, final String mergingScheme) {
        if (source == null) {
            return this;
        }

        this.name = NamedBuilder.create(this.name)
                .merge(source.getName())
                .build();
        this.owner = NamedBuilder.create(this.owner)
                .merge(source.getOwner())
                .build();
        this.superName = NamedBuilder.create(this.superName)
                .merge(source.getSuperName())
                .build();

        final EnumSet<AccessFlag> thisAccessFlags = AccessFlag.getAccessFlags(this.securitySpecifications);
        final EnumSet<AccessFlag> sourceAccessFlags = AccessFlag.getAccessFlags(source.getSecuritySpecification());

        final EnumSet<AccessFlag> mergedFlags = EnumSet.noneOf(AccessFlag.class);
        mergedFlags.addAll(thisAccessFlags);
        mergedFlags.addAll(sourceAccessFlags);

        this.securitySpecifications = AccessFlag.toSecuritySpecification(mergedFlags);

        final Map<Reference, MethodMetadata> schemadLocalMethods = this.methods
                .stream().collect(CollectorUtils.toLinkedMap(
                        mm -> ReferenceBuilder.create()
                                .withName(NamedBuilder.create()
                                        .with(mergingScheme, mm.getName().getName(mergingScheme).orElse(""))
                                )
                                .withOwner(NamedBuilder.create()
                                        .with(mergingScheme, mm.getOwner().getName(mergingScheme).orElse(""))
                                )
                                .withDescriptor(NamedBuilder.create()
                                        .with(mergingScheme, mm.getDescriptor().getName(mergingScheme).orElse(""))
                                )
                                .build(),
                        Function.identity()
                ));

        final Map<Reference, MethodMetadata> schemadSourceMethods = source.getMethods()
                .stream().collect(CollectorUtils.toLinkedMap(
                        mm -> ReferenceBuilder.create()
                                .withName(NamedBuilder.create()
                                        .with(mergingScheme, mm.getName().getName(mergingScheme).orElse(""))
                                )
                                .withOwner(NamedBuilder.create()
                                        .with(mergingScheme, mm.getOwner().getName(mergingScheme).orElse(""))
                                )
                                .withDescriptor(NamedBuilder.create()
                                        .with(mergingScheme, mm.getDescriptor().getName(mergingScheme).orElse(""))
                                )
                                .build(),
                        Function.identity()
                ));

        this.methods = new LinkedHashSet<>();
        for (final Reference keyReference : schemadLocalMethods.keySet()) {
            if (!schemadSourceMethods.containsKey(keyReference)) {
                this.methods.add(schemadLocalMethods.get(keyReference));
            } else {
                this.methods.add(
                        MethodMetadataBuilder.create(schemadLocalMethods.get(keyReference))
                                .merge(schemadSourceMethods.get(keyReference), mergingScheme)
                                .build()
                );
            }
        }
        schemadSourceMethods.keySet().stream()
                .filter(mr -> !schemadLocalMethods.containsKey(mr))
                .forEach(mr -> this.methods.add(schemadSourceMethods.get(mr)));

        final Map<Named, FieldMetadata> schemadLocalFields = this.fields
                .stream().collect(CollectorUtils.toLinkedMap(
                        fm -> NamedBuilder.create()
                                .with(mergingScheme, fm.getName().getName(mergingScheme).orElse(""))
                                .build(),
                        Function.identity()
                ));
        final Map<Named, FieldMetadata> schemadSourceFields = source.getFields()
                .stream().collect(CollectorUtils.toLinkedMap(
                        fm -> NamedBuilder.create()
                                .with(mergingScheme, fm.getName().getName(mergingScheme).orElse(""))
                                .build(),
                        Function.identity()
                ));
        this.fields = new LinkedHashSet<>();
        for (final Named keyReference : schemadLocalFields.keySet()) {
            if (!schemadSourceFields.containsKey(keyReference)) {
                this.fields.add(schemadLocalFields.get(keyReference));
            } else {
                this.fields.add(
                        FieldMetadataBuilder.create(schemadLocalFields.get(keyReference))
                                .merge(schemadSourceFields.get(keyReference))
                                .build()
                );
            }
        }
        schemadSourceFields.keySet().stream()
                .filter(mr -> !schemadLocalFields.containsKey(mr))
                .forEach(mr -> this.fields.add(schemadSourceFields.get(mr)));

        final Map<Named, RecordMetadata> schemadLocalRecords = this.records
                .stream().collect(CollectorUtils.toLinkedMap(
                        fm -> NamedBuilder.create()
                                .with(mergingScheme, fm.getField().getName().getName(mergingScheme).orElse(""))
                                .build(),
                        Function.identity()
                ));
        final Map<Named, RecordMetadata> schemadSourceRecords = source.getRecords()
                .stream().collect(CollectorUtils.toLinkedMap(
                        fm -> NamedBuilder.create()
                                .with(mergingScheme, fm.getField().getName().getName(mergingScheme).orElse(""))
                                .build(),
                        Function.identity()
                ));
        this.records = new LinkedHashSet<>();
        for (final Named keyReference : schemadLocalRecords.keySet()) {
            if (!schemadSourceRecords.containsKey(keyReference)) {
                this.records.add(schemadLocalRecords.get(keyReference));
            } else {
                this.records.add(
                        RecordMetadataBuilder.create(schemadLocalRecords.get(keyReference))
                                .merge(schemadSourceRecords.get(keyReference))
                                .build()
                );
            }
        }
        schemadSourceRecords.keySet().stream()
                .filter(mr -> !schemadLocalRecords.containsKey(mr))
                .forEach(mr -> this.records.add(schemadSourceRecords.get(mr)));

        final Map<Named, ClassMetadata> schemadLocalInnerClasses = this.innerClasses
                .stream().collect(CollectorUtils.toLinkedMap(
                        fm -> NamedBuilder.create()
                                .with(mergingScheme, fm.getName().getName(mergingScheme).orElse(""))
                                .build(),
                        Function.identity()
                ));
        final Map<Named, ClassMetadata> schemadSourceInnerClasses = source.getInnerClasses()
                .stream().collect(CollectorUtils.toLinkedMap(
                        fm -> NamedBuilder.create()
                                .with(mergingScheme, fm.getName().getName(mergingScheme).orElse(""))
                                .build(),
                        Function.identity()
                ));
        this.innerClasses = new LinkedHashSet<>();
        for (final Named keyReference : schemadLocalInnerClasses.keySet()) {
            if (!schemadSourceInnerClasses.containsKey(keyReference)) {
                this.innerClasses.add(schemadLocalInnerClasses.get(keyReference));
            } else {
                this.innerClasses.add(
                        ClassMetadataBuilder.create(schemadLocalInnerClasses.get(keyReference))
                                .merge(schemadSourceInnerClasses.get(keyReference), mergingScheme)
                                .build()
                );
            }
        }
        schemadSourceInnerClasses.keySet().stream()
                .filter(mr -> !schemadLocalInnerClasses.containsKey(mr))
                .forEach(mr -> this.innerClasses.add(schemadSourceInnerClasses.get(mr)));

        this.signature = NamedBuilder.create(this.signature).merge(source.getSignature()).build();

        return this;
    }

    @Override
    public @NonNull ClassMetadata toImmutable() {
        return build();
    }

    @NonNull
    public ClassMetadata build() {
        return new ImmutableClassMetadata(
                superName.toImmutable(),
                interfaces.stream().map(Named::toImmutable).collect(CollectorUtils.toLinkedSet()),
                methods.stream().map(MethodMetadata::toImmutable).collect(CollectorUtils.toLinkedSet()),
                fields.stream().map(FieldMetadata::toImmutable).collect(CollectorUtils.toLinkedSet()),
                records.stream().map(RecordMetadata::toImmutable).collect(CollectorUtils.toLinkedSet()),
                innerClasses.stream().map(ClassMetadata::toImmutable).collect(CollectorUtils.toLinkedSet()),
                owner.toImmutable(),
                name.toImmutable(),
                securitySpecifications,
                signature, isRecord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSuperName(), getInterfaces(), getMethods(), getFields(), getRecords(), getInnerClasses(), getOwner(),
                getName(), getSecuritySpecification());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassMetadata)) {
            return false;
        }
        ClassMetadata that = (ClassMetadata) o;
        return getSecuritySpecification() == that.getSecuritySpecification()
                && Objects.equals(getSuperName(), that.getSuperName())
                && getInterfaces().equals(that.getInterfaces())
                && getMethods().equals(that.getMethods())
                && getFields().equals(that.getFields())
                && getRecords().equals(that.getRecords())
                && getInnerClasses().equals(that.getInnerClasses())
                && Objects.equals(getOwner(), that.getOwner())
                && getName().equals(that.getName());
    }

    @Override
    public int getSecuritySpecification() {
        return securitySpecifications;
    }

    @Override
    public @NonNull Named getSuperName() {
        return superName;
    }

    @Override
    public @NonNull LinkedHashSet<Named> getInterfaces() {
        return interfaces;
    }

    @Override
    public @NonNull LinkedHashSet<MethodMetadata> getMethods() {
        return methods;
    }

    @Override
    public @NonNull LinkedHashSet<FieldMetadata> getFields() {
        return fields;
    }

    @Override
    public @NonNull LinkedHashSet<RecordMetadata> getRecords() {
        return records;
    }

    @Override
    public @NonNull LinkedHashSet<ClassMetadata> getInnerClasses() {
        return innerClasses;
    }

    @Override
    public @NonNull Named getSignature() {
        return signature;
    }

    @Override
    public boolean isRecord() {
        return isRecord;
    }

    @Override
    public @NonNull Named getOwner() {
        return owner;
    }

    @Override
    public @NonNull Named getName() {
        return name;
    }
}
