package org.parchmentmc.feather.mapping;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.parchmentmc.feather.util.SimpleVersion;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * A container for mapping data, for packages, classes, methods, fields, and parameters.
 */
public interface MappingDataContainer {
    /**
     * The current recognized format version.
     */
    SimpleVersion CURRENT_FORMAT = new SimpleVersion(1, 0, 0);

    /**
     * Returns the format version of this container.
     *
     * <p>The format version is used in JSON serialization to ensure that a serialized mapping data container is in a
     * format recognized by the program.</p>
     *
     * <p>This version number is incremented for each <em>non-backwards compatible</em> change to the JSON format. Additive
     * changes will usually not cause the version to be incremented, as JSON deserializers should ignore any unknown
     * fields.</p>
     *
     * @return the format version of this container
     * @see #CURRENT_FORMAT
     */
    SimpleVersion getFormatVersion();

    /**
     * Returns the never-{@code null} unmodifiable collection of package data stored within this container.
     *
     * @return the unmodifiable collection of package data
     */
    Collection<? extends PackageData> getPackages();

    /**
     * Returns the never-{@code null} unmodifiable collection of class data stored within this container.
     *
     * @return the unmodifiable collection of class data
     */
    Collection<? extends ClassData> getClasses();

    /**
     * Gets the package data for the given package name, or {@code null} if there is no package data available for that
     * name.
     *
     * @param packageName The package name
     * @return the package data for the given name, or {@code null} if not found
     * @see PackageData#getName()
     */
    @Nullable
    PackageData getPackage(String packageName);

    /**
     * Gets the class data for the given class name, or {@code null} if there is no class data available for that name.
     *
     * @param className The class name
     * @return the class data for the given name, or {@code null} if not found
     * @see ClassData#getName()
     */
    @Nullable
    ClassData getClass(String className);

    /**
     * Data for a Java package.
     *
     * @see MappingDataContainer
     */
    interface PackageData {
        Comparator<PackageData> COMPARATOR = Comparator.comparing(PackageData::getName);

        /**
         * Returns the name of this package.
         *
         * <p>A package name may consist of an empty string (the "default package"), or one or more identifiers
         * separated by ASCII forward slashes ({@code /}). An identifier is defined by &sect;3.8 <em>"Identifiers"</em>
         * of the JLS 8.</p>
         *
         * <p>While &sect;6.5 <em>"Determining the Meaning of a Name"</em> of the JLS 8 defines a package name to be
         * separated by ASCII periods ({@code .}), to simplify processing of names within the program, package names
         * are separated by ASCII forward slashes instead, to be similar to binary names of classes within {@code class}
         * file structures, as defined by &sect;4.2.1 <em>"Binary Class and Interface Names"</em> of the JVMS 8.</p>
         *
         * @return the name of this package
         * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.8">The Java&reg; Language
         * Specification, Java SE 8 Edition, &sect;3.8 "Identifiers"</a>
         * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-6.html#jls-6.5">The Java&reg; Language
         * Specification, Java SE 8 Edition, &sect;6.5 "Determining the Meaning of a Name"</a>
         * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.2.1">The Java&reg; Virtual
         * Machine Specification, Java SE 8 Edition, &sect;4.2.1 "Binary Class and Interface Names"</a>
         */
        String getName();

        /**
         * Returns the javadoc for this package, as a never-{@code null} unmodifiable list of strings.
         *
         * <p>If there is no javadoc for this package, then this list will be empty.</p>
         *
         * @return a possibly-empty unmodifiable list of strings as javadoc
         */
        List<String> getJavadoc();
    }

    /**
     * Data for a Java class, including but not limited to interfaces, annotations, and enumerations.
     *
     * @see MappingDataContainer
     */
    interface ClassData {
        Comparator<ClassData> COMPARATOR = Comparator.comparing(ClassData::getName);

        /**
         * Returns the name of this class.
         *
         * <p>A class name consists of one or more identifiers separated by ASCII forward slashes ({@code /}). An
         * identifier is defined by &sect;3.8 <em>"Identifiers"</em> of the JLS 8.</p>
         *
         * <p>While &sect;6.5 <em>"Determining the Meaning of a Name"</em> of the JLS 8 defines a class name to be
         * separated by ASCII periods ({@code .}), to simplify processing of names within the program, class names
         * are separated by ASCII forward slashes instead, to be equal to binary names of classes within {@code class}
         * file structures, as defined by &sect;4.2.1 <em>"Binary Class and Interface Names"</em> of the JVMS 8.</p>
         *
         * @return the name of this package
         * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.8">The Java&reg; Language
         * Specification, Java SE 8 Edition, &sect;3.8 "Identifiers"</a>
         * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-6.html#jls-6.5">The Java&reg; Language
         * Specification, Java SE 8 Edition, &sect;6.5 "Determining the Meaning of a Name"</a>
         * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.2.1">The Java&reg; Virtual
         * Machine Specification, Java SE 8 Edition, &sect;4.2.1 "Binary Class and Interface Names"</a>
         */
        String getName();

        /**
         * Returns the javadoc for this class, as a never-{@code null} unmodifiable list of strings.
         *
         * <p>If there is no javadoc for this class, then this list will be empty.</p>
         *
         * @return a possibly-empty unmodifiable list of strings as javadoc
         */
        List<String> getJavadoc();

        /**
         * Returns the never-{@code null} unmodifiable collection of field data stored within this class.
         *
         * @return the unmodifiable collection of field data
         */
        Collection<? extends FieldData> getFields();

        /**
         * Returns the never-{@code null} unmodifiable collection of method data stored within this class.
         *
         * @return the unmodifiable collection of method data
         */
        Collection<? extends MethodData> getMethods();

        /**
         * Gets the field data for the given field name, or {@code null} if there is no field data available for that
         * name.
         *
         * @param fieldName The field name
         * @return the field data for the given name, or {@code null} if not found
         * @see FieldData#getName()
         */
        @Nullable
        FieldData getField(String fieldName);

        /**
         * Gets the method data for the given method name and descriptor, or {@code null} if there is no method data
         * available for that name and descriptor combination.
         *
         * @param methodName The method name
         * @param descriptor The method descriptor
         * @return the method data for the given name and descriptor, or {@code null} if not found
         * @see MethodData#getName()
         * @see MethodData#getDescriptor()
         */
        @Nullable
        MethodData getMethod(String methodName, String descriptor);
    }

    /**
     * Data for a field in a Java class, including {@linkplain Enum enumeration} values.
     *
     * @see ClassData
     */
    interface FieldData {
        Comparator<FieldData> COMPARATOR = Comparator.comparing(FieldData::getName);

        /**
         * Returns the name of this field.
         *
         * <p>A field name is an identifier, as defined by &sect;3.8 <em>"Identifiers"</em> of the JLS 8.</p>
         *
         * @return the name of this field
         * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.8">The Java&reg; Language
         * Specification, Java SE 8 Edition, &sect;3.8 "Identifiers"</a>
         */
        String getName();

        /**
         * Returns the descriptor for this field.
         *
         * <p>A field descriptor is defined by &sect;4.3.2 <em>"Field Descriptors"</em> of the JVMS 8.</p>
         *
         * @return the field descriptor
         * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.3.2">The Java&reg; Virtual
         * Machine Specification, Java SE 8 Edition, &sect;4.3.2 "Field Descriptors"</a>
         */
        String getDescriptor();

        /**
         * Returns the javadoc for this field, as a never-{@code null} unmodifiable list of strings.
         *
         * <p>If there is no javadoc for this field, then this list will be empty.</p>
         *
         * @return a possibly-empty unmodifiable list of strings as javadoc
         */
        List<String> getJavadoc();
    }

    /**
     * Data for a method in a Java class, including the class initializer and constructors.
     *
     * @see ClassData
     */
    interface MethodData {
        Comparator<MethodData> COMPARATOR = Comparator.comparing(MethodData::getName).thenComparing(MethodData::getDescriptor);

        /**
         * Returns the name of this method.
         *
         * <p>A method name is either an identifier, as defined by &sect;3.8 <em>"Identifiers"</em> of the JLS 8, or
         * one of two special method names used within the Java Virtual Machine (as supplied by the compiler):
         * <dl>
         *     <dt>{@code <init>}</dt>
         *     <dd>The <em>instance initialization method</em>, also known as a <em>constructor</em> in the Java
         *     programming language.</dd>
         *     <dt>{@code <clinit>}</dt>
         *     <dd>The <em>class/interface initialization method</em>, also known as the class initializer.</dd>
         * </dl></p>
         *
         * <p>The two special method names are defined by &sect;2.9 <em>"Special Methods"</em> of the JVMS 8.</p>
         *
         * @return the name of this field
         * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.8">The Java&reg; Language
         * Specification, Java SE 8 Edition, &sect;3.8 "Identifiers"</a>
         * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html#jvms-2.9">The Java&reg; Virtual
         * Machine Specification, Java SE 8 Edition, &sect;2.9 "Special Methods"</a>
         */
        String getName();

        /**
         * Returns the descriptor for this method.
         *
         * <p>A method descriptor is defined by &sect;4.3.3 <em>"Method Descriptors"</em> of the JVMS 8, which consists
         * of parameter descriptors (describing the types of each method parameter) and a return descriptor (describing
         * the return type).</p>
         *
         * <p>The two special methods have special restrictions on their descriptors, as defined in &sect;2.9 <em>"Special
         * Methods"</em> of the JVMS 8.</p>
         *
         * <p>For the <em>class/interface initializatiom method</em> ({@code init}): it will always have no
         * parameters and a void return descriptor, for a method descriptor of {@code ()V}. For the <em>instance
         * initialization method</em> ({@code <init>}), also known as a <em>constructor</em> in the Java programming
         * language: by definition (&sect;8.8 <em>"Constructor Declarations"</em> of the JLS 8) it must not return a value,
         * which means that the return descriptor must be void ({@code V}).</p>
         *
         * @return the field descriptor
         * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.3.3">The Java&reg; Virtual
         * Machine Specification, Java SE 8 Edition, &sect;4.3.3 "Method Descriptors"</a>
         * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html#jvms-2.9">The Java&reg; Virtual
         * Machine Specification, Java SE 8 Edition, &sect;2.9 "Special Methods"</a>
         * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-8.html#jls-8.8">The Java&reg; Language
         * Specification, Java SE 8 Edition, &sect;8.8 "Constructor Declarations"</a>
         */
        String getDescriptor();

        /**
         * Returns the javadoc for this method, as a never-{@code null} unmodifiable list of strings.
         *
         * <p>If there is no javadoc for this method, then this list will be empty.</p>
         *
         * @return a possibly-empty unmodifiable list of strings as javadoc
         */
        List<String> getJavadoc();

        /**
         * Returns the never-{@code null} unmodifiable collection of parameter data for this method.
         *
         * @return the unmodifiable collection of parameter data
         */
        Collection<? extends ParameterData> getParameters();

        /**
         * Gets the parameter data at the given parameter index, or {@code null} if there is no parameter data available
         * at that index.
         *
         * @param index The parameter index
         * @return the parameter data for the given index, or {@code null} if not found
         * @see ParameterData#getIndex()
         */
        @Nullable
        ParameterData getParameter(byte index);
    }

    /**
     * Data for a parameter in a method in a Java class.
     *
     * @see MethodData
     */
    interface ParameterData {
        Comparator<ParameterData> COMPARATOR = Comparator.comparing(ParameterData::getIndex);

        /**
         * Returns the index of this parameter in the method signature.
         *
         * <p>A parameter's index is calculated by summing up the lengths of all parameters before it in a method's
         * signature. A {@code long} and {@code double} has a length of 2, while all other types (including arrays)
         * have a length of {@code 1}. For an instance or interface method, there is an implicit first parameter not
         * listed in the method signature, which is the instance of the type for the method.</p>
         *
         * <p>A method signature's total parameter length (that is, the sum of lengths of its parameters) must not
         * exceed {@code 255} including the implicit instance parameter.</p>
         *
         * <p>While the JVMS mandates that parameter indexes include the implicit instance parameter, users of this
         * class may ignore the instance parameter for the purposes of this index, especially in situations where
         * the user does not know if a method is a static or an instance/interface method. </p>
         *
         * @return The index of this parameter
         * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.3.3">The Java&reg; Virtual
         * Machine Specification, Java SE 8 Edition, &sect;4.3.3 "Method Descriptors"</a>
         */
        byte getIndex();

        /**
         * Returns the name of this parameter.
         *
         * <p>A parameter name is an identifier, as defined by &sect;3.8 <em>"Identifiers"</em> of the JLS 8.</p>
         *
         * @return the name of this field
         * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.8">The Java&reg; Language
         * Specification, Java SE 8 Edition, &sect;3.8 "Identifiers"</a>
         */
        @Nullable
        String getName();

        /**
         * Returns the javadoc for this parameter, for {@code null} if there is no javadoc.
         *
         * @return the javadoc for this parameter, or {@code null} if none
         */
        @Nullable
        String getJavadoc();
    }
}
