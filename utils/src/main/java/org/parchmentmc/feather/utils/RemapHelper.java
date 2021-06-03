package org.parchmentmc.feather.utils;

import java.util.Arrays;
import java.util.function.UnaryOperator;

/**
 * Helper for remapping type and method descriptors.
 */
public final class RemapHelper {
    private static final String PRIMITIVE_TYPE_DESCRIPTORS = "VICZDFJBS";

    private RemapHelper() { // Prevent instantiation
        throw new AssertionError();
    }

    /**
     * Remaps the given type descriptor with the given remapping function.
     *
     * @param typeDescriptor    the type descriptor to remap
     * @param remappingFunction the type remapping function. Returns a remapped type for a given input type, or {@code
     *                          null} if there is no remapped type.
     * @return the remapped type descriptor
     * @throws IllegalArgumentException if the type descriptor cannot be recognized: not a primitive descriptor nor a
     *                                  reference type descriptor (with optional array dimensions)
     */
    public static String remapTypeDescriptor(String typeDescriptor, UnaryOperator<String> remappingFunction) {
        if (typeDescriptor.equals("*"))
            return "*";

        if (typeDescriptor.length() == 1 && PRIMITIVE_TYPE_DESCRIPTORS.contains(typeDescriptor)) {
            return typeDescriptor; // Primitive type descriptor
        }

        if (typeDescriptor.startsWith("-") || typeDescriptor.startsWith("+")) {
            return typeDescriptor.charAt(0) + remapTypeDescriptor(
              typeDescriptor.substring(1),
              remappingFunction
            );
        }

        if (typeDescriptor.startsWith("[")) { // Dimension of an array;
            return "[" + remapTypeDescriptor(typeDescriptor.substring(1), remappingFunction);
        }

        if (typeDescriptor.startsWith("L")) { // Reference type descriptor
            if (typeDescriptor.contains("<") && typeDescriptor.contains(">")) {
                final String remappedTypeName = remappingFunction.apply(typeDescriptor.substring(1, typeDescriptor.indexOf("<")));
                final String genericArguments = typeDescriptor.substring(
                  typeDescriptor.indexOf("<") + 1,
                  typeDescriptor.lastIndexOf(">")
                );

                StringBuilder remappedGenericArgument = new StringBuilder().append('<');

                for (int charIndex = 0; charIndex < genericArguments.length(); charIndex++)
                {
                    char c = genericArguments.charAt(charIndex);
                    if (PRIMITIVE_TYPE_DESCRIPTORS.indexOf(c) > -1 || c == '[' || c == '*' || c == '-' || c == '+') {
                        remappedGenericArgument.append(c);
                    } else if (c == 'L') {
                        StringBuilder refType = new StringBuilder();
                        int openBrackets = 0;
                        do {
                            refType.append(c);
                            if (c == '<') {
                                openBrackets++;
                            }else if (c == '>') {
                                openBrackets--;
                            }

                            c = genericArguments.charAt(++charIndex);
                        } while (c != ';' || openBrackets > 0);

                        remappedGenericArgument.append(remapTypeDescriptor(refType.append(';').toString(), remappingFunction));
                    } else {
                        throw new IllegalArgumentException("Unknown descriptor " + c + " in generic descriptor: " + typeDescriptor);
                    }
                }

                remappedGenericArgument.append(">");

                return "L" + remappedTypeName + remappedGenericArgument + ";";
            }

            final String remapped = remappingFunction.apply(typeDescriptor.substring(1, typeDescriptor.length() - 1));
            if (remapped != null) {
                return "L" + remapped + ";";
            }
            return typeDescriptor;
        }

        throw new IllegalArgumentException("Unrecognizable type descriptor: " + typeDescriptor);
    }

    /**
     * Remaps the given method descriptor with the given remapping function.
     *
     * @param methodDescriptor  the method descriptor to remap
     * @param remappingFunction the type remapping function. Returns a remapped type for a given input type, or {@code
     *                          null} if there is no remapped type.
     * @return the remapped method descriptor
     * @throws IllegalArgumentException If the method descriptor is invalid; there is no enclosing parentheses pair, or
     *                                  there is an unrecognized type descriptor
     * @see #remapTypeDescriptor(String, UnaryOperator)
     */
    public static String remapMethodDescriptor(String methodDescriptor, UnaryOperator<String> remappingFunction) {
        // (<parameter type descriptors>)<return type descriptor>
        int cursor = 0;

        if (methodDescriptor.charAt(cursor++) != '(' || !methodDescriptor.contains(")")) {
            throw new IllegalArgumentException("Invalid method descriptor: " + methodDescriptor);
        }
        StringBuilder remappedOutput = new StringBuilder().append('(');

        char c;
        while ((c = methodDescriptor.charAt(cursor++)) != ')') {
            if (PRIMITIVE_TYPE_DESCRIPTORS.indexOf(c) > -1 || c == '[') {
                remappedOutput.append(c);
            } else if (c == 'L') {
                StringBuilder refType = new StringBuilder();
                int openBrackets = 0;
                do {
                    refType.append(c);
                    if (c == '<') {
                        openBrackets++;
                    }else if (c == '>') {
                        openBrackets--;
                    }

                    c = methodDescriptor.charAt(cursor++);
                } while (c != ';' || openBrackets > 0);
                remappedOutput.append(remapTypeDescriptor(refType.append(';').toString(), remappingFunction));
            } else {
                throw new IllegalArgumentException("Unknown descriptor " + c + " in method descriptor: " + methodDescriptor);
            }
        }

        String returnType = methodDescriptor.substring(cursor);
        if (returnType.isEmpty()) {
            throw new IllegalArgumentException("Invalid method descriptor: " + methodDescriptor);
        }
        remappedOutput.append(')').append(remapTypeDescriptor(returnType, remappingFunction));

        return remappedOutput.toString();
    }
}
