package org.parchmentmc.feather.manifests;

import org.parchmentmc.feather.metadata.*;
import org.parchmentmc.feather.util.SimpleVersion;

import java.lang.reflect.Modifier;
import java.util.List;

import static com.google.common.collect.ImmutableList.of;
import static org.parchmentmc.feather.manifests.TestConstantsHelper.named;
import static org.parchmentmc.feather.named.ImmutableNamed.empty;

public interface MetadataTestConstants {
    List<MethodReference> METHOD_REFERENCES = of(
            new ImmutableMethodReference(named("a"), named("<init>"),
                    named("()V"), named("()V")),
            new ImmutableMethodReference(named("a"), named("ma"),
                    named("()Ljava/util/List;"), named("()Ljava/util/List<[I>;"))
    );

    List<MethodMetadata> METHOD_METADATA = of(
            new ImmutableMethodMetadata(named("a"), false, null, of(),
                    named("ma"), Modifier.PROTECTED, named("()Ljava/util/List;"), named("()Ljava/util/List<[I>;")),
            new ImmutableMethodMetadata(named("a"), true, null, METHOD_REFERENCES,
                    named("la"), Modifier.PROTECTED, named("()F"), named("()F")),
            new ImmutableMethodMetadata(named("a"), false, METHOD_REFERENCES.get(0), of(),
                    named("xa"), Modifier.PROTECTED, named("()V"), named("()V"))
    );

    List<FieldMetadata> FIELD_METADATA = of(
            new ImmutableFieldMetadata(
                    named("a"),
                    named("aa", "testField"),
                    Modifier.PUBLIC | Modifier.STATIC,
                    named("F"),
                    named("F")
            )
            , new ImmutableFieldMetadata(
                    named("a"),
                    named("bb"),
                    Modifier.PUBLIC,
                    named("Ljava/util/List;", "Ljava/util/List;"),
                    named("Ljava/util/List<[I>;", "Ljava/util/List<[I>;")
            )
    );

    List<ClassMetadata> INNER_CLASSES = of(
            new ImmutableClassMetadata(named("a"),
                    of(named("b"), named("c", "IInterface")),
                    METHOD_METADATA, FIELD_METADATA, of(),
                    empty(), named("d"),
                    Modifier.PUBLIC | Modifier.FINAL
            ),
            new ImmutableClassMetadata(empty(),
                    of(),
                    METHOD_METADATA, FIELD_METADATA, of(),
                    empty(), named("e"),
                    Modifier.PUBLIC | Modifier.FINAL
            )
    );

    List<ClassMetadata> CLASS_METADATA = of(
            new ImmutableClassMetadata(named("f"),
                    of(named("h"), named("c", "IInterface")),
                    METHOD_METADATA, FIELD_METADATA, INNER_CLASSES,
                    empty(), named("g"),
                    Modifier.PUBLIC
            ),
            new ImmutableClassMetadata(empty(),
                    of(),
                    METHOD_METADATA, FIELD_METADATA, of(),
                    empty(), named("h"),
                    Modifier.PUBLIC
            )
    );

    List<SourceMetadata> SOURCE_METADATA = of(
            new ImmutableSourceMetadata(SimpleVersion.of(1, 0, 0), "1.16.5", CLASS_METADATA),
            new ImmutableSourceMetadata(SimpleVersion.of("1.0.0"), "21w01a", of())
    );
}

