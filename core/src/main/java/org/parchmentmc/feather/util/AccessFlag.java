package org.parchmentmc.feather.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum AccessFlag
{
    PUBLIC (0x0001), // class, field, method
    PRIVATE (0x0002), // class, field, method
    PROTECTED (0x0004), // class, field, method
    STATIC (0x0008), // field, method
    FINAL (0x0010), // class, field, method, parameter
    SUPER (0x0020), // class
    SYNCHRONIZED (0x0020), // method
    OPEN (0x0020), // module
    TRANSITIVE (0x0020), // module requires
    VOLATILE (0x0040), // field
    BRIDGE (0x0040), // method
    STATIC_PHASE (0x0040), // module requires
    VARARGS (0x0080), // method
    TRANSIENT (0x0080), // field
    NATIVE (0x0100), // method
    INTERFACE (0x0200), // class
    ABSTRACT (0x0400), // class, method
    STRICT (0x0800), // method
    SYNTHETIC (0x1000), // class, field, method, parameter, module *
    ANNOTATION (0x2000), // class
    ENUM (0x4000), // class(?) field inner
    MANDATED (0x8000), // field, method, parameter, module, module *
    MODULE (0x8000); // class

    private final int bitMask;

    AccessFlag(final int bitMask) {this.bitMask = bitMask;}

    public boolean isActive(final int bitField) {
        return (bitField & this.bitMask) == this.bitMask;
    }
    
    public static List<AccessFlag> getAccessFlags(final int bitField) {
        return Arrays.stream(values()).filter(a -> a.isActive(bitField)).collect(Collectors.toList());
    }
}
