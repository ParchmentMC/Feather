package org.parchmentmc.feather.metadata;

import com.google.common.collect.Sets;
import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.named.NamedBuilder;
import org.parchmentmc.feather.util.SimpleVersion;

import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.of;
import static org.parchmentmc.feather.named.Named.empty;
import static org.parchmentmc.feather.util.TestConstantsHelper.named;

public interface MetadataTestConstants {
    LinkedHashSet<Reference> METHOD_REFERENCES = Sets.newLinkedHashSet(of(
            new ImmutableReference(named("a"), named("<init>"),
                    named("()V"), named("()V")),
            new ImmutableReference(named("a"), named("ma"),
                    named("()Ljava/util/Set;"), named("()Ljava/util/Set<[I>;"))
    ));

    LinkedHashSet<MethodMetadata> METHOD_METADATA = new LinkedHashSet<>(of(
            new ImmutableMethodMetadata(named("a"), false, null, null, new LinkedHashSet<>(),
                    named("ma"), Modifier.PROTECTED, named("()Ljava/util/Set;"), named("()Ljava/util/Set<[I>;"), 0, 0),
            new ImmutableMethodMetadata(named("a"), true, null, new ImmutableReference(named("a"), named("<init>"),
                    named("()V"), named("()V")), METHOD_REFERENCES,
                    named("la"), Modifier.PROTECTED, named("()F"), named("()F"), 0, 0),
            new ImmutableMethodMetadata(named("a"), false, new ImmutableBouncingTargetMetadata(new ImmutableReference(named("a"), named("<init>"),
                    named("()V"), named("()V")), new ImmutableReference(named("a"), named("<init>"),
                    named("()V"), named("()V"))), null, new LinkedHashSet<>(),
                    named("xa"), Modifier.PROTECTED, named("()V"), named("()V"), 0, 0)
    ));

    LinkedHashSet<FieldMetadata> FIELD_METADATA = new LinkedHashSet<>(of(
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
                    named("Ljava/util/Set;", "Ljava/util/Set;"),
                    named("Ljava/util/Set<[I>;", "Ljava/util/Set<[I>;")
            )
    ));

    LinkedHashSet<RecordMetadata> RECORD_METADATA = new LinkedHashSet<>();

    LinkedHashSet<ClassMetadata> INNER_CLASSES = new LinkedHashSet<>(of(
            new ImmutableClassMetadata(named("a"),
                    new LinkedHashSet<>(of(named("b"), named("c", "IInterface"))),
                    METHOD_METADATA, FIELD_METADATA, RECORD_METADATA, new LinkedHashSet<>(),
                    empty(), named("d"),
                    Modifier.PUBLIC | Modifier.FINAL,
                    Named.empty(), false),
            new ImmutableClassMetadata(empty(),
                    new LinkedHashSet<>(),
                    METHOD_METADATA, FIELD_METADATA, RECORD_METADATA, new LinkedHashSet<>(),
                    empty(), named("e"),
                    Modifier.PUBLIC | Modifier.FINAL,
                    NamedBuilder.create().withObfuscated("<T>").build(), false)
    ));

    LinkedHashSet<ClassMetadata> CLASS_METADATA = new LinkedHashSet<>(of(
            new ImmutableClassMetadata(named("f"),
                    new LinkedHashSet<>(of(named("h"), named("c", "IInterface"))),
                    METHOD_METADATA, FIELD_METADATA, RECORD_METADATA, INNER_CLASSES,
                    empty(), named("g"),
                    Modifier.PUBLIC,
                    Named.empty(), false),
            new ImmutableClassMetadata(empty(),
                    new LinkedHashSet<>(),
                    METHOD_METADATA, FIELD_METADATA, RECORD_METADATA, new LinkedHashSet<>(),
                    empty(), named("h"),
                    Modifier.PUBLIC,
                    NamedBuilder.create().withObfuscated("<T>").build(), false)
    ));

    Set<SourceMetadata> SOURCE_METADATA = of(
            new ImmutableSourceMetadata(SimpleVersion.of(1, 0, 0), "1.16.5", CLASS_METADATA),
            new ImmutableSourceMetadata(SimpleVersion.of("1.0.0"), "21w01a", new LinkedHashSet<>())
    );
}

