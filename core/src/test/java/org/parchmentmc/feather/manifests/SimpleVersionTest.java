package org.parchmentmc.feather.manifests;

import org.junit.jupiter.api.Test;
import org.parchmentmc.feather.util.SimpleVersion;

import static org.junit.jupiter.api.Assertions.*;
import static org.parchmentmc.feather.util.SimpleVersion.copyOf;
import static org.parchmentmc.feather.util.SimpleVersion.of;

public class SimpleVersionTest {
    @Test
    public void testParsing() {
        assertEquals(of("1.2.3"), of(1, 2, 3));

        assertEquals(of("1.2"), of(1, 2, 0));
    }

    @Test
    public void testArgumentValidation() {
        assertThrows(IllegalArgumentException.class, () -> of(-1, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> of(0, -1, 0));
        assertThrows(IllegalArgumentException.class, () -> of(0, 0, -1));

        assertThrows(IllegalArgumentException.class, () -> of("-1.0.0"));
        assertThrows(IllegalArgumentException.class, () -> of("0.-1.0"));
        assertThrows(IllegalArgumentException.class, () -> of("0.0.-1"));

        assertThrows(IllegalArgumentException.class, () -> of("0.0.0.0"));
        assertThrows(IllegalArgumentException.class, () -> of("0"));
    }

    @Test
    public void testCopying() {
        SimpleVersion ver = of(1, 2, 3);

        assertEquals(ver, copyOf(ver));

        assertNotSame(ver, copyOf(ver));
    }

    @Test
    public void testCompatibility() {
        assertTrue(of(1, 0, 0).isCompatibleWith(of(1, 2, 8)));

        assertFalse(of(1, 5, 0).isCompatibleWith(of(2, 0, 0)));

        assertFalse(of(2, 0, 0).isCompatibleWith(of(1, 5, 0)));
    }

    @Test
    public void testComparisons() {
        assertTrue(of("1.0.0").compareTo(of("1.0.1")) < 0);
        assertTrue(of("1.1.0").compareTo(of("1.0.1")) > 0);
        assertTrue(of("1.1.0").compareTo(of("1.1.1")) < 0);
        assertEquals(0, of("1.1.1").compareTo(of("1.1.1")));
    }

    @Test
    public void testHashCodeEquals() {
        SimpleVersion verA = of(1, 0, 0);
        SimpleVersion verB = of(1, 0, 0);

        assertNotSame(verA, verB);

        assertEquals(verA, verB);

        assertEquals(verA.hashCode(), verB.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(of("1.2.3").toString(), of(1, 2, 3).toString());

        assertEquals(of("1.2").toString(), of(1, 2, 0).toString());
    }
}
