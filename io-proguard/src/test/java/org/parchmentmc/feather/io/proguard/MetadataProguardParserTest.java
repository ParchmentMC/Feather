package org.parchmentmc.feather.io.proguard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.metadata.*;
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
                                .withName(NamedBuilder.create()
                                        .withObfuscated("dnm")
                                        .withMojang("com/mojang/blaze3d/DontObfuscate")
                                        .build()
                                )
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
                                .withName(NamedBuilder.create()
                                        .withObfuscated("dnm")
                                        .withMojang("com/mojang/blaze3d/DontObfuscate")
                                        .build()
                                )
                                .build()
                        )
                        .addClass(ClassMetadataBuilder.create()
                                .withName(NamedBuilder.create()
                                        .withObfuscated("dnn")
                                        .withMojang("com/mojang/blaze3d/FieldsAreNonnullByDefault")
                                        .build()
                                )
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
                                .withName(NamedBuilder.create()
                                        .withObfuscated("dnm")
                                        .withMojang("com/mojang/blaze3d/DontObfuscate")
                                        .build()
                                )
                                .addInnerClass(
                                        ClassMetadataBuilder.create()
                                                .withName(NamedBuilder.create()
                                                        .withObfuscated("dnm$a")
                                                        .withMojang("com/mojang/blaze3d/DontObfuscate$Inner")
                                                        .build()
                                                )
                                                .withOwner(NamedBuilder.create()
                                                        .withObfuscated("dnm")
                                                        .withMojang("com/mojang/blaze3d/DontObfuscate")
                                                        .build()
                                                )
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
                        .addClass(
                                ClassMetadataBuilder.create()
                                        .withName(NamedBuilder.create()
                                                .withObfuscated("dnm")
                                                .withMojang("com/mojang/blaze3d/DontObfuscate")
                                                .build()
                                        )
                                        .addMethod(
                                                MethodMetadataBuilder.create()
                                                        .withOwner(NamedBuilder.create()
                                                                .withObfuscated("dnm")
                                                                .withMojang("com/mojang/blaze3d/DontObfuscate")
                                                                .build()
                                                        )
                                                        .withName(NamedBuilder.create()
                                                                .withObfuscated("a")
                                                                .withMojang("process")
                                                                .build()
                                                        )
                                                        .withStartLine(12)
                                                        .withEndLine(13)
                                                        .withDescriptor(NamedBuilder.create()
                                                                .withObfuscated("(Lcom/mojang/blaze3d/pipeline/RenderPipeline;F)V")
                                                                .withMojang("(Lcom/mojang/blaze3d/pipeline/RenderPipeline;F)V")
                                                                .build()
                                                        )
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
                        .addClass(
                                ClassMetadataBuilder.create()
                                        .withName(NamedBuilder.create()
                                                .withObfuscated("a")
                                                .withMojang("com/mojang/blaze3d/pipeline/RenderPipeline")
                                                .build()
                                        )
                                        .build()
                        )
                        .addClass(
                                ClassMetadataBuilder.create()
                                        .withName(NamedBuilder.create()
                                                .withObfuscated("dnm")
                                                .withMojang("com/mojang/blaze3d/DontObfuscate")
                                                .build()
                                        )
                                        .addMethod(
                                                MethodMetadataBuilder.create()
                                                        .withOwner(NamedBuilder.create()
                                                                .withObfuscated("dnm")
                                                                .withMojang("com/mojang/blaze3d/DontObfuscate")
                                                                .build()
                                                        )
                                                        .withName(NamedBuilder.create()
                                                                .withObfuscated("a")
                                                                .withMojang("process")
                                                                .build()
                                                        )
                                                        .withStartLine(12)
                                                        .withEndLine(13)
                                                        .withDescriptor(NamedBuilder.create()
                                                                .withObfuscated("(La;F)V")
                                                                .withMojang("(Lcom/mojang/blaze3d/pipeline/RenderPipeline;F)V")
                                                                .build()
                                                        )
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
                        .addClass(
                                ClassMetadataBuilder.create()
                                        .withName(NamedBuilder.create()
                                                .withObfuscated("dnm")
                                                .withMojang("com/mojang/blaze3d/DontObfuscate")
                                                .build()
                                        )
                                        .addMethod(
                                                MethodMetadataBuilder.create()
                                                        .withOwner(NamedBuilder.create()
                                                                .withObfuscated("dnm")
                                                                .withMojang("com/mojang/blaze3d/DontObfuscate")
                                                                .build()
                                                        )
                                                        .withName(NamedBuilder.create()
                                                                .withObfuscated("a")
                                                                .withMojang("process")
                                                                .build()
                                                        )
                                                        .withStartLine(12)
                                                        .withEndLine(13)
                                                        .withDescriptor(NamedBuilder.create()
                                                                .withObfuscated("(La;F)V")
                                                                .withMojang("(Lcom/mojang/blaze3d/pipeline/RenderPipeline;F)V")
                                                                .build()
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )
                        .addClass(
                                ClassMetadataBuilder.create()
                                        .withName(NamedBuilder.create()
                                                .withObfuscated("a")
                                                .withMojang("com/mojang/blaze3d/pipeline/RenderPipeline")
                                                .build()
                                        )
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
                        .addClass(
                                ClassMetadataBuilder.create()
                                        .withName(NamedBuilder.create()
                                                .withObfuscated("dnm")
                                                .withMojang("com/mojang/blaze3d/DontObfuscate")
                                                .build()
                                        )
                                        .addField(FieldMetadataBuilder.create()
                                                .withName(NamedBuilder.create()
                                                        .withObfuscated("b")
                                                        .withMojang("LOGGER")
                                                        .build()
                                                )
                                                .withOwner(NamedBuilder.create()
                                                        .withObfuscated("dnm")
                                                        .withMojang("com/mojang/blaze3d/DontObfuscate")
                                                        .build()
                                                )
                                                .withDescriptor(NamedBuilder.create()
                                                        .withObfuscated("Lorg/apache/logging/log4j/Logger;")
                                                        .withMojang("Lorg/apache/logging/log4j/Logger;")
                                                        .build()
                                                )
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
                        .addClass(
                                ClassMetadataBuilder.create()
                                        .withName(NamedBuilder.create()
                                                .withObfuscated("dnm")
                                                .withMojang("com/mojang/blaze3d/DontObfuscate")
                                                .build()
                                        )
                                        .addField(FieldMetadataBuilder.create()
                                                .withName(NamedBuilder.create()
                                                        .withObfuscated("b")
                                                        .withMojang("LOGGER")
                                                        .build()
                                                )
                                                .withOwner(NamedBuilder.create()
                                                        .withObfuscated("dnm")
                                                        .withMojang("com/mojang/blaze3d/DontObfuscate")
                                                        .build()
                                                )
                                                .withDescriptor(NamedBuilder.create()
                                                        .withObfuscated("La;")
                                                        .withMojang("Lorg/apache/logging/log4j/Logger;")
                                                        .build()
                                                )
                                                .build()
                                        )
                                        .build()
                        )
                        .addClass(
                                ClassMetadataBuilder.create()
                                        .withName(NamedBuilder.create()
                                                .withObfuscated("a")
                                                .withMojang("org/apache/logging/log4j/Logger")
                                                .build()
                                        )
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
                        .addClass(
                                ClassMetadataBuilder.create()
                                        .withName(NamedBuilder.create()
                                                .withObfuscated("a")
                                                .withMojang("org/apache/logging/log4j/Logger")
                                                .build()
                                        )
                                        .build()
                        )
                        .addClass(
                                ClassMetadataBuilder.create()
                                        .withName(NamedBuilder.create()
                                                .withObfuscated("dnm")
                                                .withMojang("com/mojang/blaze3d/DontObfuscate")
                                                .build()
                                        )
                                        .addField(FieldMetadataBuilder.create()
                                                .withName(NamedBuilder.create()
                                                        .withObfuscated("b")
                                                        .withMojang("LOGGER")
                                                        .build()
                                                )
                                                .withOwner(NamedBuilder.create()
                                                        .withObfuscated("dnm")
                                                        .withMojang("com/mojang/blaze3d/DontObfuscate")
                                                        .build()
                                                )
                                                .withDescriptor(NamedBuilder.create()
                                                        .withObfuscated("La;")
                                                        .withMojang("Lorg/apache/logging/log4j/Logger;")
                                                        .build()
                                                )
                                                .build()
                                        )
                                        .build()
                        )
                        .build(),
                "Fields are not parsed properly."
        );
    }
}