package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;

import java.util.LinkedHashSet;
import java.util.Objects;

final class ImmutableClassMetadata implements ClassMetadata {
    private final Named superName;
    private final LinkedHashSet<Named> interfaces;
    private final LinkedHashSet<MethodMetadata> methods;
    private final LinkedHashSet<FieldMetadata> fields;
    private final LinkedHashSet<RecordMetadata> records;
    private final LinkedHashSet<ClassMetadata> innerClasses;

    private final Named owner;
    private final Named name;
    private final int securitySpecifications;
    private final Named                   signature;
    private final boolean isRecord;

    public ImmutableClassMetadata(
      final Named superName,
      final LinkedHashSet<Named> interfaces,
      final LinkedHashSet<MethodMetadata> methods,
      final LinkedHashSet<FieldMetadata> fields,
      final LinkedHashSet<RecordMetadata> records, final LinkedHashSet<ClassMetadata> innerClasses,
      final Named owner,
      final Named name,
      final int securitySpecifications, final Named signature, final boolean isRecord) {
        this.superName = superName;
        this.interfaces = new LinkedHashSet<>(interfaces);
        this.methods = new LinkedHashSet<>(methods);
        this.fields = new LinkedHashSet<>(fields);
        this.records = new LinkedHashSet<>(records);
        this.innerClasses = new LinkedHashSet<>(innerClasses);
        this.owner = owner;
        this.name = name;
        this.securitySpecifications = securitySpecifications;
        this.signature = signature;
        this.isRecord = isRecord;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSuperName(), getInterfaces(), getMethods(), getFields(), getInnerClasses(), getOwner(),
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
    public @NonNull LinkedHashSet<RecordMetadata> getRecords()
    {
        return records;
    }

    @Override
    public @NonNull LinkedHashSet<ClassMetadata> getInnerClasses() {
        return innerClasses;
    }

    @Override
    public @NonNull Named getSignature()
    {
        return signature;
    }

    @Override
    public boolean isRecord()
    {
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

    @Override
    public String toString() {
        return "ImmutableClassMetadata{" +
                "superName=" + superName +
                ", interfaces=" + interfaces +
                ", methods=" + methods +
                ", fields=" + fields +
                ", innerClasses=" + innerClasses +
                ", owner=" + owner +
                ", name=" + name +
                ", securitySpecifications=" + securitySpecifications +
                '}';
    }

    @Override
    public @NonNull ClassMetadata toImmutable() {
        return this;
    }
}
