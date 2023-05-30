package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.named.NamedBuilder;
import org.parchmentmc.feather.util.CollectorUtils;
import org.parchmentmc.feather.util.SimpleVersion;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public final class SourceMetadataBuilder implements SourceMetadata {
    private SimpleVersion specVersion = SimpleVersion.of(1, 0, 0);
    private String minecraftVersion = "0.0.0";
    private LinkedHashSet<ClassMetadata> classes = new LinkedHashSet<>();

    private SourceMetadataBuilder() {
    }

    public static SourceMetadataBuilder create() {
        return new SourceMetadataBuilder();
    }

    public static SourceMetadataBuilder create(final SourceMetadata target) {
        if (target == null)
            return create();

        return create()
                .withSpecVersion(target.getSpecificationVersion())
                .withMinecraftVersion(target.getMinecraftVersion())
                .withClasses(target.getClasses());
    }

    public SourceMetadataBuilder withSpecVersion(SimpleVersion specVersion) {
        this.specVersion = specVersion;
        return this;
    }

    public SourceMetadataBuilder withMinecraftVersion(String minecraftVersion) {
        this.minecraftVersion = minecraftVersion;
        return this;
    }

    public SourceMetadataBuilder withClasses(LinkedHashSet<ClassMetadata> classes) {
        this.classes = classes;
        return this;
    }

    public SourceMetadataBuilder merge(final SourceMetadata source, final String mergingSchema) {
        if (source == null)
            return this;

        final Map<Named, ClassMetadata> schemadLocalInnerClasses = this.classes
                .stream().collect(CollectorUtils.toLinkedMap(
                        fm -> NamedBuilder.create()
                                .with(mergingSchema, fm.getName().getName(mergingSchema).orElse(""))
                                .build(),
                        Function.identity()
                ));
        final Map<Named, ClassMetadata> schemadSourceInnerClasses = source.getClasses()
                .stream().collect(CollectorUtils.toLinkedMap(
                        fm -> NamedBuilder.create()
                                .with(mergingSchema, fm.getName().getName(mergingSchema).orElse(""))
                                .build(),
                        Function.identity()
                ));
        this.classes = new LinkedHashSet<>();
        for (final Named keyReference : schemadLocalInnerClasses.keySet()) {
            if (!schemadSourceInnerClasses.containsKey(keyReference)) {
                this.classes.add(schemadLocalInnerClasses.get(keyReference));
            } else {
                this.classes.add(
                        ClassMetadataBuilder.create(schemadLocalInnerClasses.get(keyReference))
                                .merge(schemadSourceInnerClasses.get(keyReference), mergingSchema)
                                .build()
                );
            }
        }
        schemadSourceInnerClasses.keySet().stream()
                .filter(mr -> !schemadLocalInnerClasses.containsKey(mr))
                .forEach(mr -> this.classes.add(schemadSourceInnerClasses.get(mr)));

        return this;
    }

    @Override
    public SimpleVersion getSpecificationVersion() {
        return specVersion;
    }

    @Override
    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    @Override
    public LinkedHashSet<ClassMetadata> getClasses() {
        return classes;
    }

    public ImmutableSourceMetadata build() {
        return new ImmutableSourceMetadata(
                specVersion,
                minecraftVersion,
                classes.stream().map(ClassMetadata::toImmutable).collect(CollectorUtils.toLinkedSet())
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SourceMetadata)) return false;
        SourceMetadata that = (SourceMetadata) o;
        return getSpecificationVersion().equals(that.getSpecificationVersion())
                && getMinecraftVersion().equals(that.getMinecraftVersion())
                && getClasses().equals(that.getClasses());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSpecificationVersion(), getMinecraftVersion(), getClasses());
    }

    public SourceMetadataBuilder addClass(final ClassMetadata build) {
        this.classes.add(build);
        return this;
    }

    @Override
    public @NonNull SourceMetadata toImmutable() {
        return build();
    }
}
