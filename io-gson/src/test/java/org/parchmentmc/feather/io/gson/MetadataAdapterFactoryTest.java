package org.parchmentmc.feather.io.gson;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.io.gson.metadata.MetadataAdapterFactory;
import org.parchmentmc.feather.metadata.*;
import org.parchmentmc.feather.named.ImmutableNamed;
import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.util.Constants;
import org.parchmentmc.feather.util.SimpleVersion;

import java.lang.reflect.Modifier;
import java.util.List;

import static com.google.common.collect.ImmutableList.of;

public class MetadataAdapterFactoryTest extends GSONTest {

    private static Named named(String value) {
        return new ImmutableNamed(Constants.Names.OBFUSCATED, value);
    }

    private static Named named(String valueA, String valueB) {
        return new ImmutableNamed(ImmutableMap.of(Constants.Names.OBFUSCATED, valueA, Constants.Names.MOJANG, valueB));
    }

    private static final List<MethodReference> METHOD_REFERENCES = of(
            new ImmutableMethodReference(named("a"), named("<init>"),
                    named("()V"), named("()V")),
            new ImmutableMethodReference(named("a"), named("ma"),
                    named("()Ljava/util/List;"), named("()Ljava/util/List<[I>;"))
    );

    private static final List<MethodMetadata> METHOD_METADATA = of(
            new ImmutableMethodMetadata(named("a"), false, null, of(),
                    named("ma"), Modifier.PROTECTED, named("()Ljava/util/List;"), named("()Ljava/util/List<[I>;")),
            new ImmutableMethodMetadata(named("a"), true, null, METHOD_REFERENCES,
                    named("la"), Modifier.PROTECTED, named("()F"), named("()F")),
            new ImmutableMethodMetadata(named("a"), false, METHOD_REFERENCES.get(0), of(),
                    named("xa"), Modifier.PROTECTED, named("()V"), named("()V"))
    );

    private static final List<FieldMetadata> FIELD_METADATA = of(
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

    private static final List<ClassMetadata> INNER_CLASSES = of(
            new ImmutableClassMetadata(named("a"),
                    of(named("b"), named("c", "IInterface")),
                    METHOD_METADATA, FIELD_METADATA, of(),
                    null, named("d"),
                    Modifier.PUBLIC | Modifier.FINAL
            ),
            new ImmutableClassMetadata(null,
                    of(),
                    METHOD_METADATA, FIELD_METADATA, of(),
                    null, named("e"),
                    Modifier.PUBLIC | Modifier.FINAL
            )
    );

    private static final List<ClassMetadata> CLASS_METADATA = of(
            new ImmutableClassMetadata(named("f"),
                    of(named("h"), named("c", "IInterface")),
                    METHOD_METADATA, FIELD_METADATA, INNER_CLASSES,
                    null, named("g"),
                    Modifier.PUBLIC
            ),
            new ImmutableClassMetadata(null,
                    of(),
                    METHOD_METADATA, FIELD_METADATA, of(),
                    null, named("h"),
                    Modifier.PUBLIC
            )
    );

    private static final List<SourceMetadata> SOURCE_METADATA = of(
            new ImmutableSourceMetadata(SimpleVersion.of(1, 0, 0), "1.16.5", CLASS_METADATA),
            new ImmutableSourceMetadata(SimpleVersion.of("1.0.0"), "21w01a", of())
    );

    public MetadataAdapterFactoryTest() {
        super(b -> b.registerTypeAdapter(SimpleVersion.class, new SimpleVersionAdapter())
                .registerTypeAdapterFactory(new MetadataAdapterFactory()));
    }

    @Test
    public void testMethodReferences() {
        METHOD_REFERENCES.forEach(data -> test(MethodReference.class, data));
    }

    @Test
    public void testMethodMetadata() {
        METHOD_METADATA.forEach(data -> test(MethodMetadata.class, data));
    }

    @Test
    public void testFieldMetadata() {
        FIELD_METADATA.forEach(data -> test(FieldMetadata.class, data));
    }

    @Test
    public void testClassMetadata() {
        CLASS_METADATA.forEach(data -> test(ClassMetadata.class, data));
    }

    @Test
    public void testSourceMetadata() {
        SOURCE_METADATA.forEach(data -> test(SourceMetadata.class, data));
    }
}
