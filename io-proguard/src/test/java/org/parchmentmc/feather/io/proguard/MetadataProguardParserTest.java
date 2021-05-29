package org.parchmentmc.feather.io.proguard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.metadata.*;
import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.named.NamedBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class MetadataProguardParserTest {

    @Test
    void empty_lines_produces_no_classes() {
        final List<String> empty = Collections.emptyList();

        runTest(
                empty,
                SourceMetadataBuilder.create().build(),
                "Empty lines should produce an empty SourceMetadata."
        );
    }

    private void runTest(final List<String> lines, final SourceMetadata expected, final String message) {
        final SourceMetadata target = MetadataProguardParser.fromLines(lines);
        Assertions.assertEquals(expected, target, message);
    }

    @Test
    void commented_lines_are_ignored() {
        final List<String> commentedLines = new ArrayList<>();
        commentedLines.add("# Some comment");

        runTest(
                commentedLines,
                SourceMetadataBuilder.create().build(),
                "Comments should not alter a SourceMetadata."
        );
    }

    @Test
    void starts_with_method_throws() {
        final List<String> lines = new ArrayList<>();
        lines.add("    10:10:void <init>() -> <init>");

        Assertions.assertThrows(
                IllegalStateException.class,
                () -> MetadataProguardParser.fromLines(lines),
                "Method line without a class does not throw an IllegalStateException."
        );
    }

    @Test
    void starts_with_field_throws() {
        final List<String> lines = new ArrayList<>();
        lines.add("    org.apache.logging.log4j.Logger LOGGER -> b");

        Assertions.assertThrows(
                IllegalStateException.class,
                () -> MetadataProguardParser.fromLines(lines),
                "Field line without a class does not throw an IllegalStateException."
        );
    }

    @Test
    void class_line_produces_class() {
        final List<String> lines = new ArrayList<>();
        lines.add("com.mojang.blaze3d.DontObfuscate -> dnm:");

        runTest(
                lines,
                SourceMetadataBuilder.create()
                        .addClass(ClassMetadataBuilder.create()
                                .withName(named("dnm", "com/mojang/blaze3d/DontObfuscate"))
                                .build()
                        )
                        .build(),
                "Class line does not produce a proper class."
        );
    }

    @Test
    void multiple_class_lines_produce_multiple_classes() {
        final List<String> lines = new ArrayList<>();
        lines.add("com.mojang.blaze3d.DontObfuscate -> dnm:");
        lines.add("com.mojang.blaze3d.FieldsAreNonnullByDefault -> dnn:");

        runTest(
                lines,
                SourceMetadataBuilder.create()
                        .addClass(ClassMetadataBuilder.create()
                                .withName(named("dnm", "com/mojang/blaze3d/DontObfuscate"))
                                .build()
                        )
                        .addClass(ClassMetadataBuilder.create()
                                .withName(named("dnn", "com/mojang/blaze3d/FieldsAreNonnullByDefault"))
                                .build()
                        )
                        .build(),
                "Multiple class lines do not produce multiple classes."
        );
    }

    @Test
    void inner_classes_are_processed_properly() {
        final List<String> lines = new ArrayList<>();
        lines.add("com.mojang.blaze3d.DontObfuscate -> dnm:");
        lines.add("com.mojang.blaze3d.DontObfuscate$Inner -> dnm$a:");

        runTest(
                lines,
                SourceMetadataBuilder.create()
                        .addClass(ClassMetadataBuilder.create()
                                .withName(named("dnm", "com/mojang/blaze3d/DontObfuscate"))
                                .addInnerClass(ClassMetadataBuilder.create()
                                        .withName(named("dnm$a", "com/mojang/blaze3d/DontObfuscate$Inner"))
                                        .withOwner(named("dnm", "com/mojang/blaze3d/DontObfuscate"))
                                        .build()
                                )
                                .build()
                        )
                        .build(),
                "Inner classes are not parsed properly."
        );
    }

    @Test
    void method_is_parsed_properly() {
        final List<String> lines = new ArrayList<>();
        lines.add("com.mojang.blaze3d.DontObfuscate -> dnm:");
        lines.add("    12:13:void process(com.mojang.blaze3d.pipeline.RenderPipeline,float) -> a");

        runTest(
                lines,
                SourceMetadataBuilder.create()
                        .addClass(ClassMetadataBuilder.create()
                                .withName(named("dnm", "com/mojang/blaze3d/DontObfuscate"))
                                .addMethod(MethodMetadataBuilder.create()
                                        .withOwner(named("dnm", "com/mojang/blaze3d/DontObfuscate"))
                                        .withName(named("a", "process"))
                                        .withStartLine(12)
                                        .withEndLine(13)
                                        .withDescriptor(named(
                                                "(Lcom/mojang/blaze3d/pipeline/RenderPipeline;F)V",
                                                "(Lcom/mojang/blaze3d/pipeline/RenderPipeline;F)V"
                                        ))
                                        .build()
                                )
                                .build()
                        )
                        .build(),
                "Method is not parsed properly."
        );
    }

    @Test
    void method_descriptor_is_remapped_properly_when_class_is_before_is_parsed_properly() {
        final List<String> lines = new ArrayList<>();
        lines.add("com.mojang.blaze3d.pipeline.RenderPipeline -> a:");
        lines.add("com.mojang.blaze3d.DontObfuscate -> dnm:");
        lines.add("    12:13:void process(com.mojang.blaze3d.pipeline.RenderPipeline,float) -> a");

        runTest(
                lines,
                SourceMetadataBuilder.create()
                        .addClass(ClassMetadataBuilder.create()
                                .withName(named("a", "com/mojang/blaze3d/pipeline/RenderPipeline"))
                                .build()
                        )
                        .addClass(ClassMetadataBuilder.create()
                                .withName(named("dnm", "com/mojang/blaze3d/DontObfuscate"))
                                .addMethod(MethodMetadataBuilder.create()
                                        .withOwner(named("dnm", "com/mojang/blaze3d/DontObfuscate"))
                                        .withName(named("a", "process"))
                                        .withStartLine(12)
                                        .withEndLine(13)
                                        .withDescriptor(named(
                                                "(La;F)V",
                                                "(Lcom/mojang/blaze3d/pipeline/RenderPipeline;F)V"
                                        ))
                                        .build()
                                )
                                .build()
                        )
                        .build(),
                "Method descriptors are not remapped."
        );
    }

    @Test
    void method_descriptor_is_remapped_properly_when_class_is_after_is_parsed_properly() {
        final List<String> lines = new ArrayList<>();
        lines.add("com.mojang.blaze3d.DontObfuscate -> dnm:");
        lines.add("    12:13:void process(com.mojang.blaze3d.pipeline.RenderPipeline,float) -> a");
        lines.add("com.mojang.blaze3d.pipeline.RenderPipeline -> a:");

        runTest(
                lines,
                SourceMetadataBuilder.create()
                        .addClass(ClassMetadataBuilder.create()
                                .withName(named("dnm", "com/mojang/blaze3d/DontObfuscate"))
                                .addMethod(MethodMetadataBuilder.create()
                                        .withOwner(named("dnm", "com/mojang/blaze3d/DontObfuscate"))
                                        .withName(named("a", "process"))
                                        .withStartLine(12)
                                        .withEndLine(13)
                                        .withDescriptor(named(
                                                "(La;F)V",
                                                "(Lcom/mojang/blaze3d/pipeline/RenderPipeline;F)V"
                                        ))
                                        .build()
                                )
                                .build()
                        )
                        .addClass(ClassMetadataBuilder.create()
                                .withName(named("a", "com/mojang/blaze3d/pipeline/RenderPipeline"))
                                .build()
                        )
                        .build(),
                "Method descriptors are not remapped."
        );
    }

    @Test
    void field_is_parsed_properly() {
        final List<String> lines = new ArrayList<>();
        lines.add("com.mojang.blaze3d.DontObfuscate -> dnm:");
        lines.add("    org.apache.logging.log4j.Logger LOGGER -> b");

        runTest(
                lines,
                SourceMetadataBuilder.create()
                        .addClass(ClassMetadataBuilder.create()
                                .withName(named("dnm", "com/mojang/blaze3d/DontObfuscate"))
                                .addField(FieldMetadataBuilder.create()
                                        .withName(named("b", "LOGGER"))
                                        .withOwner(named("dnm", "com/mojang/blaze3d/DontObfuscate"))
                                        .withDescriptor(named(
                                                "Lorg/apache/logging/log4j/Logger;",
                                                "Lorg/apache/logging/log4j/Logger;"
                                        ))
                                        .build()
                                )
                                .build()
                        )
                        .build(),
                "Fields are not parsed properly."
        );
    }

    @Test
    void field_is_remapped_properly_when_class_is_after_parsed_properly() {
        final List<String> lines = new ArrayList<>();
        lines.add("com.mojang.blaze3d.DontObfuscate -> dnm:");
        lines.add("    org.apache.logging.log4j.Logger LOGGER -> b");
        lines.add("org.apache.logging.log4j.Logger -> a:");

        runTest(
                lines,
                SourceMetadataBuilder.create()
                        .addClass(ClassMetadataBuilder.create()
                                .withName(named("dnm", "com/mojang/blaze3d/DontObfuscate"))
                                .addField(FieldMetadataBuilder.create()
                                        .withName(named("b", "LOGGER"))
                                        .withOwner(named("dnm", "com/mojang/blaze3d/DontObfuscate"))
                                        .withDescriptor(named(
                                                "La;",
                                                "Lorg/apache/logging/log4j/Logger;"
                                        ))
                                        .build()
                                )
                                .build()
                        )
                        .addClass(ClassMetadataBuilder.create()
                                .withName(named("a", "org/apache/logging/log4j/Logger"))
                                .build()
                        )
                        .build(),
                "Fields are not parsed properly."
        );
    }

    @Test
    void field_is_remapped_properly_when_class_is_before_parsed_properly() {
        final List<String> lines = new ArrayList<>();
        lines.add("org.apache.logging.log4j.Logger -> a:");
        lines.add("com.mojang.blaze3d.DontObfuscate -> dnm:");
        lines.add("    org.apache.logging.log4j.Logger LOGGER -> b");

        runTest(
                lines,
                SourceMetadataBuilder.create()
                        .addClass(ClassMetadataBuilder.create()
                                .withName(named("a", "org/apache/logging/log4j/Logger"))
                                .build()
                        )
                        .addClass(ClassMetadataBuilder.create()
                                .withName(named("dnm", "com/mojang/blaze3d/DontObfuscate"))
                                .addField(FieldMetadataBuilder.create()
                                        .withName(named("b", "LOGGER"))
                                        .withOwner(named("dnm", "com/mojang/blaze3d/DontObfuscate"))
                                        .withDescriptor(named(
                                                "La;",
                                                "Lorg/apache/logging/log4j/Logger;"
                                        ))
                                        .build()
                                )
                                .build()
                        )
                        .build(),
                "Fields are not parsed properly."
        );
    }

    static Named named(String obf, String mojang) {
        return NamedBuilder.create()
                .withObfuscated(obf)
                .withMojang(mojang)
                .build();
    }
}