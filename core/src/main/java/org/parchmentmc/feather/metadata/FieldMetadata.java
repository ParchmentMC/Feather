package org.parchmentmc.feather.metadata;

public interface FieldMetadata
{
    String getObfuscatedName();

    String getObfuscatedDescriptor();

    String getObfuscatedSignature();

    String getMojMapName();

    String getMojMapDescriptor();

    String getMojMapSignature();

    Integer getAccessSpecification();
}
