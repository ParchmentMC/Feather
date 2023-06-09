package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.named.NamedBuilder;

public final class RecordMetadataBuilder implements RecordMetadata {
    private Named owner = Named.empty();
    private Reference field = null;
    private Reference getter = null;

    private RecordMetadataBuilder() {
    }

    public static RecordMetadataBuilder create(final RecordMetadata target) {
        if (target == null) {
            return create();
        }

        return create()
                .withOwner(target.getOwner())
                .withField(target.getField())
                .withGetter(target.getGetter());
    }

    public static RecordMetadataBuilder create() {
        return new RecordMetadataBuilder();
    }

    public RecordMetadataBuilder withGetter(final Reference getter) {
        this.getter = getter;
        return this;
    }

    public RecordMetadataBuilder withField(final Reference field) {
        this.field = field;
        return this;
    }

    public RecordMetadataBuilder withOwner(final Named owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public @NonNull Named getOwner() {
        return owner;
    }

    @Override
    public Reference getField() {
        return field;
    }

    @Override
    public Reference getGetter() {
        return getter;
    }

    @Override
    public @NonNull RecordMetadata toImmutable() {
        return build();
    }

    @NonNull
    public RecordMetadataBuilder merge(final RecordMetadata source) {
        if (source == null)
            return this;

        this.owner = NamedBuilder.create(this.owner)
                .merge(source.getOwner())
                .build();
        this.field = ReferenceBuilder.create(this.field)
                .merge(source.getField())
                .build();
        this.getter = ReferenceBuilder.create(this.getter)
                .merge(source.getGetter())
                .build();

        return this;
    }

    public RecordMetadata build() {
        return new ImmutableRecordMetadata(
                owner.toImmutable(),
                field.toImmutable(),
                getter.toImmutable()
        );
    }


}
