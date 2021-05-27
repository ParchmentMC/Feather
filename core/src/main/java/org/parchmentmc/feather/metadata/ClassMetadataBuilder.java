package org.parchmentmc.feather.metadata;

import com.google.common.collect.Sets;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.qual.A;
import org.parchmentmc.feather.named.ImmutableNamed;
import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.named.NamedBuilder;
import org.parchmentmc.feather.util.AccessFlag;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ClassMetadataBuilder implements ClassMetadata {
    private Named superName = ImmutableNamed.empty();
    private Set<Named> interfaces = Sets.newHashSet();
    private Set<MethodMetadata> methods = Sets.newHashSet();
    private Set<FieldMetadata> fields = Sets.newHashSet();
    private Set<ClassMetadata> innerClasses = Sets.newHashSet();
    private Named owner = ImmutableNamed.empty();
    private Named name = ImmutableNamed.empty();
    private int securitySpecifications = 0;

    private ClassMetadataBuilder() {
    }

    public static ClassMetadataBuilder create() {
        return new ClassMetadataBuilder();
    }

    public static ClassMetadataBuilder create(final ClassMetadata classMetadata) {
        return create()
          .withSuperName(classMetadata.getSuperName())
          .withInterfaces(classMetadata.getInterfaces())
          .withMethods(classMetadata.getMethods())
          .withFields(classMetadata.getFields())
          .withInnerClasses(classMetadata.getInnerClasses())
          .withOwner(classMetadata.getOwner())
          .withName(classMetadata.getName())
          .withSecuritySpecifications(classMetadata.getSecuritySpecification());
    }

    public ClassMetadataBuilder withSuperName(Named superName) {
        this.superName = superName;
        return this;
    }

    public ClassMetadataBuilder withInterfaces(Set<Named> interfaces) {
        this.interfaces = new HashSet<>(interfaces);
        return this;
    }

    public ClassMetadataBuilder withMethods(Set<MethodMetadata> methods) {
        this.methods = new HashSet<>(methods);
        return this;
    }

    public ClassMetadataBuilder addMethod(final MethodMetadata method)
    {
        this.methods.add(method);
        return this;
    }

    public ClassMetadataBuilder withFields(Set<FieldMetadata> fields) {
        this.fields = new HashSet<>(fields);
        return this;
    }

    public ClassMetadataBuilder addField(final FieldMetadata field)
    {
        this.fields.add(field);
        return this;
    }

    public ClassMetadataBuilder withInnerClasses(Set<ClassMetadata> innerClasses) {
        this.innerClasses = new HashSet<>(innerClasses);
        return this;
    }

    public ClassMetadataBuilder addInnerClass(final ClassMetadata classMetadata)
    {
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

    public ClassMetadataBuilder merge(final ClassMetadata source, final String mergingScheme)
    {
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

        final Map<MethodReference, MethodMetadata> schemadLocalMethods = this.methods
          .stream().collect(
            Collectors.toMap(
              mm -> MethodReferenceBuilder.create()
                      .withName(
                        NamedBuilder.create()
                          .with(
                            mergingScheme,
                            mm.getName().getName(mergingScheme).orElse("")
                          )
                      )
                      .withOwner(
                        NamedBuilder.create()
                          .with(
                            mergingScheme,
                            mm.getOwner().getName(mergingScheme).orElse("")
                          )
                      )
                      .withDescriptor(
                        NamedBuilder.create()
                          .with(
                            mergingScheme,
                            mm.getDescriptor().getName(mergingScheme).orElse("")
                          )
                      )
                      .withSignature(
                        NamedBuilder.create()
                          .with(
                            mergingScheme,
                            mm.getSignature().getName(mergingScheme).orElse("")
                          )
                      )
                      .build(),
              Function.identity()
            )
          );

        final Map<MethodReference, MethodMetadata> schemadSourceMethods = source.getMethods()
                                                                           .stream().collect(
            Collectors.toMap(
              mm -> MethodReferenceBuilder.create()
                      .withName(
                        NamedBuilder.create()
                          .with(
                            mergingScheme,
                            mm.getName().getName(mergingScheme).orElse("")
                          )
                      )
                      .withOwner(
                        NamedBuilder.create()
                          .with(
                            mergingScheme,
                            mm.getOwner().getName(mergingScheme).orElse("")
                          )
                      )
                      .withDescriptor(
                        NamedBuilder.create()
                          .with(
                            mergingScheme,
                            mm.getDescriptor().getName(mergingScheme).orElse("")
                          )
                      )
                      .withSignature(
                        NamedBuilder.create()
                          .with(
                            mergingScheme,
                            mm.getSignature().getName(mergingScheme).orElse("")
                          )
                      )
                      .build(),
              Function.identity()
            )
          );

        this.methods = new HashSet<>();
        for (final MethodReference keyReference : schemadLocalMethods.keySet())
        {
            if (!schemadSourceMethods.containsKey(keyReference))
            {
                this.methods.add(schemadLocalMethods.get(keyReference));
            }
            else
            {
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
          .stream().collect(
            Collectors.toMap(
              fm -> NamedBuilder.create()
                .with(mergingScheme, fm.getName().getName(mergingScheme).orElse(""))
                .build(),
              Function.identity()
            )
          );
        final Map<Named, FieldMetadata> schemadSourceFields = source.getFields()
                                                               .stream().collect(
            Collectors.toMap(
              fm -> NamedBuilder.create()
                      .with(mergingScheme, fm.getName().getName(mergingScheme).orElse(""))
                      .build(),
              Function.identity()
            )
          );
        this.fields = new HashSet<>();
        for (final Named keyReference : schemadLocalFields.keySet())
        {
            if (!schemadSourceFields.containsKey(keyReference))
            {
                this.fields.add(schemadLocalFields.get(keyReference));
            }
            else
            {
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

        final Map<Named, ClassMetadata> schemadLocalInnerClasses = this.innerClasses
                                                               .stream().collect(
            Collectors.toMap(
              fm -> NamedBuilder.create()
                      .with(mergingScheme, fm.getName().getName(mergingScheme).orElse(""))
                      .build(),
              Function.identity()
            )
          );
        final Map<Named, ClassMetadata> schemadSourceInnerClasses = source.getInnerClasses()
                                                                .stream().collect(
            Collectors.toMap(
              fm -> NamedBuilder.create()
                      .with(mergingScheme, fm.getName().getName(mergingScheme).orElse(""))
                      .build(),
              Function.identity()
            )
          );
        this.innerClasses = new HashSet<>();
        for (final Named keyReference : schemadLocalInnerClasses.keySet())
        {
            if (!schemadSourceInnerClasses.containsKey(keyReference))
            {
                this.innerClasses.add(schemadLocalInnerClasses.get(keyReference));
            }
            else
            {
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

        return this;
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

    @NonNull
    public ImmutableClassMetadata build() {
        return new ImmutableClassMetadata(
          superName.toImmutable(),
          interfaces.stream().map(Named::toImmutable).collect(Collectors.toSet()),
          methods.stream().map(MethodMetadata::toImmutable).collect(Collectors.toSet()),
          fields.stream().map(FieldMetadata::toImmutable).collect(Collectors.toSet()),
          innerClasses.stream().map(ClassMetadata::toImmutable).collect(Collectors.toSet()),
          owner.toImmutable(),
          name.toImmutable(),
          securitySpecifications);
    }

    @Override
    public @NonNull ClassMetadata toImmutable()
    {
        return build();
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
