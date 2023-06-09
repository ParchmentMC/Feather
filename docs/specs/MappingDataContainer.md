# Mapping Data Container Specification

A mapping data container (also known as **MDC**, and represented by the `MappingDataContainer` class) is a container of javadocs for packages, classes, fields, methods, and parameters, and parameter names. It contains five inner data classes which are used to represented the stored data for each type listed previously, respectively: `PackageData`, `ClassData`, `FieldData`, `MethodData`, and `ParameterData`.

_This page documents the mapping data container specification as of Feather 0.4.0._

A mapping data container contains a collection of package data and a collection of class data. These collections may be queried by package name and class name, respectively.

Package data consists of the package name (in internal binary form, such as `com/example/test/pkg`), and optionally javadocs in the form of a list of strings. The package name must be present.

Class data consists of the class name (in internal binary form, such as `com/example/test/TestingClass`), a collection of field data, a collection of method data, and optionally javadocs in the form of a list of strings. These collections may be queried by field name and method name and descriptor, respectively. The class name must be present.

Field data consists of the field name, the field type descriptor, and optionally javadocs in the form of a list of strings. The field name and field type descriptior must be present.

Method data consists of the method name, the method descriptor, a collection of parameter data, and optionally javadocs in the form of a list of strings. The collection may be queried by parameter index. The method name and method descriptor must be present.

Parameter data consists of the parameter index as a byte, optionally a name for the parameter, and optionally javadocs as a string. The parameter index must be present. The parameter index is determined by counting its position, where the first parameter's index is 0 for static methods, and 1 for non-static methods (because 0 is the implicit `this` parameter), and counting two positions for `long`s and `double`s.

For javadocs in the form of a list of strings, each string in the list represents one line (each line is implicitly terminated by a newline). Consumers may reformat the javadocs data in any way they see fit, such as limiting combining them into paragraphs and splitting them to a preferred line length. Consumers shall be reponsible for appending the parameter javadocs to the method javadocs.

## Format version

A specialization of a mapping data container exists solely for serialization: the versioned mapping data contianer (also known as **versioned MDC**, represented by the `VersionedMappingDataContainer` class). This contains one extra field of information: a format version, which is a `SimpleVersion` object. This format version has three parts: the major version, the minor version, and the patch version, in the form of `<major>.<minor>[.<patch>]`. The patch version can be omitted, in which case it will default to a value of zero. For each change to the format, this version field is incremented accordingly:

- For minor changes which do not break compatibility nor add new data, the patch version is incremented. _(It is expected that this field will usually never be incremented.)_
- For changes which add new data without breaking compatibility with parsers for previous versions of the format, the minor version is incremented.
- For changes which break compatibility with parsers for previous versions of the format, the major version is incremented.

For example, a parser for a version `1.0.1` should be able to read versioned mapping data containers with versions of `1.0.0`, `1.1.0`, `1.3.4`, but not necessarily be able to read one with version `2.0.0`.

## Parser requirements

A parser whose highest supported version is X.Y.Z must support reading and writing any format version whose X is equal. A parser may throw an exception when trying to read a version whose major version they do not support, or when trying to read a version whose minor version does not match. A parser must not throw an exception for unrecognized keys.

## JSON format

Unless otherwise specified, all keys must have non-null values and must be present. If they may not be present, the key must not appear instead of containing a null value. If the key must be present, non-null and specifies an array, the array must be an empty array instead of a null.

```yaml
{
  "version": "1.0.0" # version string, required for versioned MDCs; must be in the form of "(\d+)\.(\d+)\.(\d+)"
    "packages": [ # Collection of package data
      { # A single package data
        "name": "com/example/test/pkg", # Package name in internal binary form
        "javadoc": [ "Testing package", "", "Some docs" ] # Javadocs, may be not present
      },
      { # A package data without javadocs
        "name": "com/example/undocumented"
      }
    ],
  "classes": [ # Collection of class data
    { # A single class data
      "name": "com/example/test/TestingClass", # Class name in internal binary form
      "javadoc": [ "Testing class" ], # Javadocs, may be not present
      "fields": [ # Collection of field data
        {
          "name": "myField", # Field name
          "descriptor": "Ljava/util/List;", # Field type descriptor
          "javadoc": [ "A field containing a list", "", "Who knows what it holds." ] # Javadocs, may be not present
        }
      ],
      "methods": [ # Collection of method data
        {
          "name": "doSomething", # Method name
          "descriptor": "(I)Ljava/lang/Object;", # Method descriptor
          "javadoc": [ "Does something" ], # Javadocs, may be not present
          "parameters": [ # Collection of parameter data
            {
              "index": 0, # Parameter index (this indicates the method is static)
              "name": "myParam", # Parameter name, may be not present
              "javadoc": "a number parameter" # Javadoc, may be not present
            }
          ]
        }
      ]
    }
  ]
}
```