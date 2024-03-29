package org.parchmentmc.feather.utils;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.parchmentmc.feather.utils.RemapHelper.remapMethodDescriptor;
import static org.parchmentmc.feather.utils.RemapHelper.remapTypeDescriptor;

public class RemapHelperTest {
    @Test
    void throw_on_illegal_descriptors() {
        assertThrows(IllegalArgumentException.class, () -> remapTypeDescriptor("Qtest;", s -> null));
        assertThrows(IllegalArgumentException.class, () -> remapTypeDescriptor("Y", s -> null));
        assertThrows(IllegalArgumentException.class, () -> remapTypeDescriptor("$", s -> null));

        assertThrows(IllegalArgumentException.class, () -> remapMethodDescriptor("(V", s -> null));
        assertThrows(IllegalArgumentException.class, () -> remapMethodDescriptor("II)V", s -> null));
        assertThrows(IllegalArgumentException.class, () -> remapMethodDescriptor("S(I)V", s -> null));
        assertThrows(IllegalArgumentException.class, () -> remapMethodDescriptor("(J)", s -> null));
    }

    @Test
    void shortcut_for_primitive_type_descriptors() {
        assertEquals("V", remapTypeDescriptor("V", s -> null));
        assertEquals("Z", remapTypeDescriptor("Z", s -> null));
        assertEquals("B", remapTypeDescriptor("B", s -> null));
        assertEquals("C", remapTypeDescriptor("C", s -> null));
        assertEquals("S", remapTypeDescriptor("S", s -> null));
        assertEquals("I", remapTypeDescriptor("I", s -> null));
        assertEquals("F", remapTypeDescriptor("F", s -> null));
        assertEquals("J", remapTypeDescriptor("J", s -> null));
        assertEquals("D", remapTypeDescriptor("D", s -> null));
    }

    static final Map<String, String> REMAPS = ImmutableMap.of(
            "a", "com/example/Remapped",
            "com/example/Original", "com/example/Target"
    );

    @Test
    void remap_reference_type_descriptors() {
        assertEquals("La;", remapTypeDescriptor("La;", s -> null));
        assertEquals("Lcom/example/Remapped;", remapTypeDescriptor("La;", REMAPS::get));
        assertEquals("[Lcom/example/Remapped;", remapTypeDescriptor("[La;", REMAPS::get));
        assertEquals("Lcom/example/Remapped;", remapTypeDescriptor("Lcom/example/Remapped;", REMAPS::get));
        assertEquals("Lcom/example/Target;", remapTypeDescriptor("Lcom/example/Original;", REMAPS::get));
        assertEquals("[[Lcom/example/Target;", remapTypeDescriptor("[[Lcom/example/Original;", REMAPS::get));
        assertEquals("Lcom/example/Original;", remapTypeDescriptor("Lcom/example/Original;", s -> null));
    }

    @Test
    void remap_method_descriptors() {
        assertEquals("()V", remapMethodDescriptor("()V", s -> null));
        assertEquals("(III)J", remapMethodDescriptor("(III)J", s -> null));
        assertEquals("(II[Z)La;", remapMethodDescriptor("(II[Z)La;", s -> null));
        assertEquals("(II[Z)Lcom/example/Remapped;", remapMethodDescriptor("(II[Z)La;", REMAPS::get));
        assertEquals("([[Lcom/example/Remapped;[Lcom/example/Target;Z)[[[C",
                remapMethodDescriptor("([[La;[Lcom/example/Original;Z)[[[C", REMAPS::get));
    }
}
