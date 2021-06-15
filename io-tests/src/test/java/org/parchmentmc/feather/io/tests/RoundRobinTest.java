package org.parchmentmc.feather.io.tests;

import java.util.ServiceLoader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.parchmentmc.feather.spi.IOAdapter;
import org.parchmentmc.feather.spi.IOAdapterFactory;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class RoundRobinTest {

    private static final ServiceLoader<IOAdapterFactory> CONVERTERS = ServiceLoader.load(IOAdapterFactory.class);

    /**
     * Tests the given object using the adapter for the given class.
     *
     * @param typeClass The class of the object under test
     * @param original  The original object
     * @param <T>       The type of the object under test
     * @see #test(IOAdapter, IOAdapter, Object)
     */
    protected <T> void test(Class<T> typeClass, T original) {
        Combinations.pairs(CONVERTERS.iterator(), (l, r) -> test(l.create(typeClass), r.create(typeClass), original));
    }

    /**
     * Tests the given pair of adapters using the given object.
     *
     * <p>This method performs assertions based on the given pair of adapters and object, and asserts that:</p>
     * <ol>
     *     <li>The adapter A {@linkplain Assertions#assertDoesNotThrow(ThrowingSupplier) do not throw an exception}
     *     while serializing the original object.</li>
     *     <li>The adapter B {@linkplain Assertions#assertDoesNotThrow(ThrowingSupplier) do not throw an exception}
     *     while serializing the original object.</li>
     *     <li>The adapter A does not throw an exception while deserializing the resulting JSON from adapter B</li>
     *     <li>The adapter B does not throw an exception while deserializing the resulting JSON from adapter A</li>
     *     <li>The adapter A does not throw an exception while serializing the resulting object from adapter B</li>
     *     <li>The adapter B does not throw an exception while serializing the resulting object from adapter A</li>
     *     <li>The original object and the resulting object from assertion #3 {@linkplain
     *     Assertions#assertEquals(Object, Object) are equal}.</li>
     *     <li>The resulting objects from assertions #3 and #4 are equal.</li>
     *     <li>The original object and the resulting object from assertion #3 {@linkplain
     *     Assertions#assertNotSame(Object, Object) are not the same object}.</li>
     *     <li>The resulting objects from assertions #3 and #4 are not the same
     *     object.</li>
     *     <li>The resulting JSONs from adapter A for assertions #1 and #5 are equal.</li>
     *     <li>The resulting JSONs from adapter B for assertions #2 and #6 are equal.</li>
     * </ol>
     *
     * @param adapterA The adapter for the object under test
     * @param adapterB The adapter for the object under test
     * @param original The original object
     * @param <T>      The type of the object under test
     */
    protected <T> void test(IOAdapter<T> adapterA, IOAdapter<T> adapterB, T original) {
        String testName = adapterA.name() + "<->" + adapterB.name();

        final String originalJsonA = assertDoesNotThrow(() -> adapterA.toJson(original), adapterA.name());
        final String originalJsonB = assertDoesNotThrow(() -> adapterB.toJson(original), adapterB.name());

        final T versionA = assertDoesNotThrow(() -> adapterA.fromJson(originalJsonB), testName);
        final T versionB = assertDoesNotThrow(() -> adapterB.fromJson(originalJsonA), testName);

        final String versionAJson = assertDoesNotThrow(() -> adapterA.toJson(versionB), testName);
        final String versionBJson = assertDoesNotThrow(() -> adapterB.toJson(versionA), testName);

        assertEquals(original, versionA);
        assertEquals(versionA, versionB);

        assertNotSame(original, versionA);
        assertNotSame(versionA, versionB);

        assertEquals(originalJsonA, versionAJson);
        assertEquals(originalJsonB, versionBJson);
    }

}
