package org.parchmentmc.feather.io.gson.metadata;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.parchmentmc.feather.io.gson.NamedAdapter;
import org.parchmentmc.feather.metadata.*;
import org.parchmentmc.feather.named.Named;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

/**
 * GSON adapter factory for {@link Named} and the metadata classes.
 *
 * @see SourceMetadata
 * @see ClassMetadata
 * @see MethodMetadata
 * @see Reference
 * @see FieldMetadata
 * @see BouncingTargetMetadata
 * @see RecordMetadata
 */
public class MetadataAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Type type = typeToken.getType();
        if (type instanceof WildcardType) {
            WildcardType wildcard = (WildcardType) type;
            if (wildcard.getUpperBounds().length == 1) { // ? extends SomeType
                type = wildcard.getUpperBounds()[0];
            }
        }

        if (type.equals(Named.class)) {
            return (TypeAdapter<T>) new NamedAdapter();
        } else if (type.equals(SourceMetadata.class)) {
            return (TypeAdapter<T>) new SourceMetadataAdapter(gson);
        } else if (type.equals(ClassMetadata.class)) {
            return (TypeAdapter<T>) new ClassMetadataAdapter(gson);
        } else if (type.equals(FieldMetadata.class)) {
            return (TypeAdapter<T>) new FieldMetadataAdapter(gson);
        } else if (type.equals(MethodMetadata.class)) {
            return (TypeAdapter<T>) new MethodMetadataAdapter(gson);
        } else if (type.equals(Reference.class)) {
            return (TypeAdapter<T>) new ReferenceAdapter(gson);
        } else if (type.equals(BouncingTargetMetadata.class)) {
            return (TypeAdapter<T>) new BouncingTargetMetadataAdapter(gson);
        } else if (type.equals(RecordMetadata.class)) {
            return (TypeAdapter<T>) new RecordMetadataAdapter(gson);
        }

        return null;
    }
}
