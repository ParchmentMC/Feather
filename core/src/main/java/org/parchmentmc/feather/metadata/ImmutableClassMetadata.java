package org.parchmentmc.feather.metadata;

import com.google.common.collect.ImmutableSet;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;

import java.util.Set;
import java.util.Objects;

public class ImmutableClassMetadata implements ClassMetadata {
    private final Named superName;
    private final Set<Named> interfaces;
    private final Set<MethodMetadata> methods;
    private final Set<FieldMetadata> fields;
    private final Set<ClassMetadata> innerClasses;
    private final Named owner;
    private final Named name;
    private final int securitySpecifications;

    public ImmutableClassMetadata(
            final Named superName,
            final Set<Named> interfaces,
            final Set<MethodMetadata> methods,
            final Set<FieldMetadata> fields,
            final Set<ClassMetadata> innerClasses, final Named owner, final Named name, final int securitySpecifications) {
        this.superName = superName;
        this.interfaces = ImmutableSet.copyOf(interfaces);
        this.methods = ImmutableSet.copyOf(methods);
        this.fields = ImmutableSet.copyOf(fields);
        this.innerClasses = ImmutableSet.copyOf(innerClasses);
        this.owner = owner;
        this.name = name;
        this.securitySpecifications = securitySpecifications;
    }

    @Override
    public @NonNull Named getSuperName() {
        return superName;
    }

    @Override
    public @NonNull Set<Named> getInterfaces() {
        return interfaces;
    }

    @Override
    public @NonNull Set<MethodMetadata> getMethods() {
        return methods;
    }

    @Override
    public @NonNull Set<FieldMetadata> getFields() {
        return fields;
    }

    @Override
    public @NonNull Set<ClassMetadata> getInnerClasses() {
        return innerClasses;
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
        return securitySpecifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassMetadata)) return false;
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
    public int hashCode() {
        return Objects.hash(getSuperName(), getInterfaces(), getMethods(), getFields(), getInnerClasses(), getOwner(),
                getName(), getSecuritySpecification());
    }

    @Override
    public String toString()
    {
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
    public @NonNull ClassMetadata toImmutable()
    {
        return this;
    }
}
