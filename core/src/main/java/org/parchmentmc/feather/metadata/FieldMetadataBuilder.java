package org.parchmentmc.feather.metadata;

import org.parchmentmc.feather.named.Named;

public final class FieldMetadataBuilder
{
    private Named owner;
    private Named name;
    private int   securitySpecification;
    private Named descriptor;
    private Named signature;

    private FieldMetadataBuilder() {}

    public static FieldMetadataBuilder anImmutableFieldMetadata() { return new FieldMetadataBuilder(); }

    public FieldMetadataBuilder withOwner(Named owner)
    {
        this.owner = owner;
        return this;
    }

    public FieldMetadataBuilder withName(Named name)
    {
        this.name = name;
        return this;
    }

    public FieldMetadataBuilder withSecuritySpecification(int securitySpecification)
    {
        this.securitySpecification = securitySpecification;
        return this;
    }

    public FieldMetadataBuilder withDescriptor(Named descriptor)
    {
        this.descriptor = descriptor;
        return this;
    }

    public FieldMetadataBuilder withSignature(Named signature)
    {
        this.signature = signature;
        return this;
    }

    public ImmutableFieldMetadata build() { return new ImmutableFieldMetadata(owner, name, securitySpecification, descriptor, signature); }
}
