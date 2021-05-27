package org.parchmentmc.feather.utils;

import org.parchmentmc.feather.metadata.*;
import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.named.NamedBuilder;
import org.parchmentmc.feather.util.Constants;

public final class MetadataMerger
{

    private MetadataMerger()
    {
        throw new IllegalStateException("Can not instantiate an instance of: MetadataMerger. This is a utility class");
    }

    public static SourceMetadata merge(
      final SourceMetadata target,
      final SourceMetadata source,
      final String mergingSchema
    ) {
        return SourceMetadataBuilder.create(target)
          .merge(source, mergingSchema)
          .build();
    }

    public static SourceMetadata mergeOnObfuscatedNames(
      final SourceMetadata target,
      final SourceMetadata source
    ) {
        return merge(
          target,
          source,
          Constants.Names.OBFUSCATED
        );
    }

    public static SourceMetadata mergeOnMojangNames(
      final SourceMetadata target,
      final SourceMetadata source
    ) {
        return merge(
          target,
          source,
          Constants.Names.MOJANG
        );
    }

    public static ClassMetadata merge(
      final ClassMetadata target,
      final ClassMetadata source,
      final String mergingSchema
    ) {
        return ClassMetadataBuilder.create(target)
                 .merge(source, mergingSchema)
                 .build();
    }

    public static ClassMetadata mergeOnObfuscatedNames(
      final ClassMetadata target,
      final ClassMetadata source
    ) {
        return merge(
          target,
          source,
          Constants.Names.OBFUSCATED
        );
    }

    public static ClassMetadata mergeOnMojangNames(
      final ClassMetadata target,
      final ClassMetadata source
    ) {
        return merge(
          target,
          source,
          Constants.Names.MOJANG
        );
    }

    public static MethodMetadata merge(
      final MethodMetadata target,
      final MethodMetadata source,
      final String mergingSchema
    ) {
        return MethodMetadataBuilder.create(target)
                 .merge(source, mergingSchema)
                 .build();
    }

    public static MethodMetadata mergeOnObfuscatedName(
      final MethodMetadata target,
      final MethodMetadata source
    ) {
        return merge(
          target,
          source,
          Constants.Names.OBFUSCATED
        );
    }

    public static MethodMetadata mergeOnMojangName(
      final MethodMetadata target,
      final MethodMetadata source
    ) {
        return merge(
          target,
          source,
          Constants.Names.MOJANG
        );
    }

    public static FieldMetadata merge(
      final FieldMetadata target,
      final FieldMetadata source
    ) {
        return FieldMetadataBuilder.create(target)
          .merge(source)
          .build();
    }

    public static Named merge(
      final Named target,
      final Named source
    ) {
        return NamedBuilder.create(target)
          .merge(source)
          .build();
    }
}
