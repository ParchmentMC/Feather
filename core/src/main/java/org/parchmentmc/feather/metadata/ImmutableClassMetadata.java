package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;

import java.util.List;

public class ImmutableClassMetadata implements ClassMetadata
{
    private final Named                superName;
    private final List<Named>          interfaces;
    private final List<MethodMetadata> methods;
    private final List<FieldMetadata>  fields;
    private final List<ClassMetadata>  innerClasses;
    private final Named                owner;
    private final Named                name;
    private final int                  securitySpecifications;

    public ImmutableClassMetadata(
      final Named superName,
      final List<Named> interfaces,
      final List<MethodMetadata> methods,
      final List<FieldMetadata> fields,
      final List<ClassMetadata> innerClasses, final Named owner, final Named name, final int securitySpecifications)
    {
        this.superName = superName;
        this.interfaces = interfaces;
        this.methods = methods;
        this.fields = fields;
        this.innerClasses = innerClasses;
        this.owner = owner;
        this.name = name;
        this.securitySpecifications = securitySpecifications;
    }

    @Override
    public @NonNull Named getSuperName()
    {
        return superName;
    }

    @Override
    public @NonNull List<Named> getInterfaces()
    {
        return interfaces;
    }

    @Override
    public @NonNull List<MethodMetadata> getMethods()
    {
        return methods;
    }

    @Override
    public @NonNull List<FieldMetadata> getFields()
    {
        return fields;
    }

    @Override
    public @NonNull List<ClassMetadata> getInnerClasses()
    {
        return innerClasses;
    }

    @Override
    public @NonNull Named getOwner()
    {
        return owner;
    }

    @Override
    public @NonNull Named getName()
    {
        return name;
    }

    @Override
    public int getSecuritySpecification()
    {
        return securitySpecifications;
    }
}
