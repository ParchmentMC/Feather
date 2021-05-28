package org.parchmentmc.feather.io.proguard;

import org.parchmentmc.feather.metadata.*;
import org.parchmentmc.feather.named.Named;
import org.parchmentmc.feather.named.NamedBuilder;
import org.parchmentmc.feather.utils.RemappableDescriptor;
import org.parchmentmc.feather.utils.RemappableType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public final class MetadataProguardParser {
    private MetadataProguardParser() {
        throw new IllegalStateException("Can not instantiate an instance of: MetadataProguardParser. This is a utility class");
    }

    public static SourceMetadata fromFile(final File file) {
        try (final InputStream inputStream = new FileInputStream(file)) {
            return fromInputStream(inputStream);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(
                    "The file: " + file.getAbsolutePath() + " does not exist. Can not parse the metadata from a none existing ProGuard obfuscation file.", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read the file: " + file.getAbsolutePath(), e);
        }
    }

    public static SourceMetadata fromInputStream(final InputStream inputStream) {
        final List<String> lines = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines()
                .filter(l -> !l.isEmpty()) //Remove Empty lines
                .collect(Collectors.toList());

        return fromLines(lines);
    }

    public static SourceMetadata fromLines(final List<String> lines) {
        final SourceMetadataBuilder sourceMetadataBuilder = SourceMetadataBuilder.create();

        ClassMetadataBuilder classMetadataBuilder = null;

        final List<String> linesContainingData = lines.stream().map(MetadataProguardParser::stripComment).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        for (final String line : linesContainingData) {
            String normalizedLine = line.replace(".", "/").trim();
            if (!normalizedLine.startsWith("    ") && normalizedLine.endsWith(":")) {
                if (classMetadataBuilder != null) {
                    sourceMetadataBuilder.addClass(classMetadataBuilder.build());
                }
                classMetadataBuilder = ClassMetadataBuilder.create();

                String[] classNameParts = normalizedLine.substring(0, normalizedLine.length() - 1).split(" -> ");
                classMetadataBuilder.withName(NamedBuilder.create().withObfuscated(classNameParts[1].trim()).withMojang(classNameParts[0].trim()));
                classMetadataBuilder.withOwner(extractOuterClassIfPresent(classMetadataBuilder.getName()));
            } else if (normalizedLine.contains("(") && normalizedLine.contains(")")) {
                if (classMetadataBuilder == null) {
                    throw new IllegalStateException("Found method line before any class name was found.");
                }

                final MethodMetadataBuilder methodMetadataBuilder = MethodMetadataBuilder.create();
                if (normalizedLine.contains(":")) {
                    final int startLineIndicationSplitterIndex = normalizedLine.indexOf(":");
                    final int endLineIndicationSplitterIndex = normalizedLine.indexOf(":", startLineIndicationSplitterIndex + 1);

                    methodMetadataBuilder.withStartLine(
                            Integer.parseInt(
                                    normalizedLine.substring(0, startLineIndicationSplitterIndex)
                            )
                    );
                    methodMetadataBuilder.withEndLine(
                            Integer.parseInt(
                                    normalizedLine.substring(startLineIndicationSplitterIndex + 1, endLineIndicationSplitterIndex)
                            )
                    );

                    normalizedLine = normalizedLine.substring(endLineIndicationSplitterIndex + 1);
                }

                final String obfuscatedName = normalizedLine.split(" -> ")[1];
                final String returnType = convertTypeToJVMDescriptor(
                        normalizedLine.split(" ")[0]
                );
                final String mojangName = normalizedLine.substring(
                        normalizedLine.indexOf(" ") + 1,
                        normalizedLine.indexOf("(")
                );
                final String[] arguments = normalizedLine.substring(
                        normalizedLine.indexOf('(') + 1,
                        normalizedLine.indexOf(')')
                ).split(",");

                final Named name = NamedBuilder.create()
                        .withObfuscated(obfuscatedName)
                        .withMojang(mojangName)
                        .build();
                final Named ownerName = classMetadataBuilder.getName();

                final String mojMapDescriptor = String.format("(%s)%s", Arrays.stream(arguments)
                                .filter(a -> !a.isEmpty())
                                .map(MetadataProguardParser::convertTypeToJVMDescriptor)
                                .collect(Collectors.joining()),
                        returnType);

                final Named descriptor = NamedBuilder.create().withMojang(mojMapDescriptor).build();

                methodMetadataBuilder.withName(name).withOwner(ownerName).withDescriptor(descriptor);

                classMetadataBuilder.addMethod(methodMetadataBuilder.build());
            } else {
                if (classMetadataBuilder == null) {
                    throw new IllegalStateException("Found method line before any class name was found.");
                }

                final FieldMetadataBuilder fieldMetadataBuilder = FieldMetadataBuilder.create();

                final String[] lineParts = normalizedLine.split(" ");
                final Named fieldName = NamedBuilder.create()
                        .withObfuscated(lineParts[3])
                        .withMojang(lineParts[1])
                        .build();

                final Named descriptor = NamedBuilder.create()
                        .withMojang(convertTypeToJVMDescriptor(lineParts[0]))
                        .build();

                fieldMetadataBuilder.withOwner(classMetadataBuilder.getName()).withName(fieldName).withDescriptor(descriptor);

                classMetadataBuilder.addField(fieldMetadataBuilder.build());
            }
        }

        if (classMetadataBuilder != null)
            sourceMetadataBuilder.addClass(classMetadataBuilder.build());

        final SourceMetadata baseDataSet = sourceMetadataBuilder.build();

        return adaptClassTypes(baseDataSet);
    }

    private static SourceMetadata adaptClassTypes(final SourceMetadata sourceMetadata) {
        final SourceMetadata typeAdapted = adaptTypes(sourceMetadata);

        return adaptInnerOuterClassList(typeAdapted);
    }

    private static SourceMetadata adaptInnerOuterClassList(final SourceMetadata sourceMetadata) {
        final Map<Named, ClassMetadataBuilder> namedClassMetadataMap = sourceMetadata.getClasses()
                .stream()
                .collect(Collectors.toMap(
                        WithName::getName,
                        ClassMetadataBuilder::create
                ));

        namedClassMetadataMap.values().forEach(classMetadata -> {
            final Named outerName = classMetadata.getOwner();
            if (namedClassMetadataMap.containsKey(outerName)) {
                final ClassMetadataBuilder outerBuilder = namedClassMetadataMap.get(outerName);
                outerBuilder.addInnerClass(classMetadata);
            }
        });

        return SourceMetadataBuilder.create()
                .withSpecVersion(sourceMetadata.getSpecificationVersion())
                .withMinecraftVersion(sourceMetadata.getMinecraftVersion())
                .withClasses(namedClassMetadataMap.values()
                        .stream()
                        .filter(classMetadataBuilder -> classMetadataBuilder.getOwner().isEmpty())
                        .map(ClassMetadataBuilder::build)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
                )
                .build();
    }

    private static SourceMetadata adaptTypes(final SourceMetadata sourceMetadata) {
        final Map<String, String> mojToObfClassNameMap = new HashMap<>();
        for (final ClassMetadata aClass : sourceMetadata.getClasses()) {
            mojToObfClassNameMap.put(
                    aClass.getName().getMojangName().orElseThrow(() -> new IllegalStateException("Missing mojang name")),
                    aClass.getName().getObfuscatedName().orElseThrow(() -> new IllegalStateException("Missing obfuscated name"))
            );
        }

        final SourceMetadataBuilder sourceMetadataBuilder = SourceMetadataBuilder.create();

        sourceMetadataBuilder.withSpecVersion(sourceMetadata.getSpecificationVersion())
                .withMinecraftVersion(sourceMetadata.getMinecraftVersion());

        for (final ClassMetadata aClass : sourceMetadata.getClasses()) {
            sourceMetadataBuilder.addClass(
                    adaptClassTypes(
                            aClass,
                            mojToObfClassNameMap
                    )
            );
        }

        return sourceMetadataBuilder.build();
    }

    private static ClassMetadata adaptClassTypes(final ClassMetadata classMetadata, final Map<String, String> mojToObfClassMap) {
        final ClassMetadataBuilder classMetadataBuilder = ClassMetadataBuilder.create();

        return classMetadataBuilder
                .withName(classMetadata.getName())
                .withSuperName(classMetadata.getSuperName())
                .withInterfaces(classMetadata.getInterfaces())
                .withInnerClasses(classMetadata.getInnerClasses().stream()
                        .map(innerClassMetadata -> adaptClassTypes(innerClassMetadata, mojToObfClassMap))
                        .collect(Collectors.toSet())
                )
                .withOwner(classMetadata.getOwner())
                .withSecuritySpecifications(classMetadata.getSecuritySpecification())
                .withMethods(
                        classMetadata
                                .getMethods()
                                .stream()
                                .map(method -> {
                                    final MethodMetadataBuilder methodMetadataBuilder = MethodMetadataBuilder.create();

                                    final RemappableDescriptor remappableDescriptor =
                                            new RemappableDescriptor(method.getDescriptor().getMojangName().orElseThrow(() -> new IllegalStateException("Missing mojang descriptor")));

                                    return methodMetadataBuilder
                                            .withName(method.getName())
                                            .withOwner(method.getOwner())
                                            .withLambda(method.isLambda())
                                            .withSecuritySpecification(method.getSecuritySpecification())
                                            .withDescriptor(NamedBuilder.create()
                                                    .withMojang(remappableDescriptor.toString())
                                                    .withObfuscated(remappableDescriptor
                                                            .remap(type -> Optional.ofNullable(
                                                                    mojToObfClassMap.get(type)
                                                            ))
                                                            .toString()
                                                    )
                                                    .build()
                                            )
                                            .withStartLine(method.getStartLine().orElse(0))
                                            .withEndLine(method.getEndLine().orElse(0))
                                            .build();
                                })
                                .collect(Collectors.toSet())
                )
                .withFields(
                        classMetadata
                                .getFields()
                                .stream()
                                .map(field -> {
                                    final FieldMetadataBuilder fieldMetadataBuilder = FieldMetadataBuilder.create();

                                    final RemappableType remappableType =
                                            new RemappableType(field.getDescriptor().getMojangName().orElseThrow(() -> new IllegalStateException("Missing mojang type.")));

                                    return fieldMetadataBuilder
                                            .withName(field.getName())
                                            .withOwner(field.getOwner())
                                            .withSecuritySpecification(field.getSecuritySpecification())
                                            .withDescriptor(
                                                    NamedBuilder.create()
                                                            .withMojang(remappableType.toString())
                                                            .withObfuscated(remappableType
                                                                    .remap(type -> Optional.ofNullable(
                                                                            mojToObfClassMap.get(type)
                                                                    ))
                                                                    .toString()
                                                            )
                                                            .build()
                                            )
                                            .build();
                                })
                                .collect(Collectors.toSet())
                )
                .build();
    }

    private static String stripComment(String line) {
        int poundIndex = line.indexOf('#');
        if (poundIndex == 0) {
            return "";
        }
        if (poundIndex != -1) {
            line = line.substring(0, poundIndex - 1);
        }
        int end = line.length();
        while (end > 1 && line.charAt(end - 1) == ' ') {
            end--;
        }
        return end == 0 ? "" : line.substring(0, end);
    }

    private static String convertTypeToJVMDescriptor(String type) {
        if (type.endsWith("[]")) { // one-dimension of an array
            return "[" + convertTypeToJVMDescriptor(type.substring(0, type.length() - 2));
        }
        switch (type) { // primitive type
            case "void":
                return "V";
            case "boolean":
                return "Z";
            case "byte":
                return "B";
            case "char":
                return "C";
            case "short":
                return "S";
            case "int":
                return "I";
            case "float":
                return "F";
            case "long":
                return "J";
            case "double":
                return "D";
        }
        if (type.contains("/")) { // reference type
            return "L" + type + ";";
        }
        throw new RuntimeException("Invalid ProGuard Descriptor: " + type);
    }

    private static Named extractOuterClassIfPresent(final Named named) {
        final NamedBuilder builder = NamedBuilder.create();

        named.getNames().forEach((schema, name) -> {
            if (name.contains("$")) {
                final String ownerName = name.substring(0, name.lastIndexOf("$"));
                builder.with(schema, ownerName);
            }
        });

        return builder;
    }
}
