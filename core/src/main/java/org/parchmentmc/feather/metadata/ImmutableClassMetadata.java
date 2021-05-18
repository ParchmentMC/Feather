package org.parchmentmc.feather.metadata;

import com.google.common.collect.ImmutableList;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;

import java.util.List;
import java.util.Objects;

public class ImmutableClassMetadata implements ClassMetadata {
    private final Named superName;
    private final List<Named> interfaces;
    private final List<MethodMetadata> methods;
    private final List<FieldMetadata> fields;
    private final List<ClassMetadata> innerClasses;
    private final Named owner;
    private final Named name;
    private final int securitySpecifications;

    public ImmutableClassMetadata(
            final Named superName,
            final List<Named> interfaces,
            final List<MethodMetadata> methods,
            final List<FieldMetadata> fields,
            final List<ClassMetadata> innerClasses, final Named owner, final Named name, final int securitySpecifications) {
        this.superName = superName;
        this.interfaces = ImmutableList.copyOf(interfaces);
        this.methods = ImmutableList.copyOf(methods);
        this.fields = ImmutableList.copyOf(fields);
        this.innerClasses = ImmutableList.copyOf(innerClasses);
        this.owner = owner;
        this.name = name;
        this.securitySpecifications = securitySpecifications;
    }

    @Override
    public @NonNull Named getSuperName() {
        return superName;
    }

    @Override
    public @NonNull List<Named> getInterfaces() {
        return interfaces;
    }

    @Override
    public @NonNull List<MethodMetadata> getMethods() {
        return methods;
    }

    @Override
    public @NonNull List<FieldMetadata> getFields() {
        return fields;
    }

    @Override
    public @NonNull List<ClassMetadata> getInnerClasses() {
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
}
