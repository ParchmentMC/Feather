package org.parchmentmc.feather.io.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Superclass for tests which use GSON functionality (such as de/serialization tests).
 */
public class GSONTest {
    protected final Gson gson;

    protected GSONTest(Consumer<GsonBuilder> builderConfig) {
        final GsonBuilder builder = new GsonBuilder();
        builderConfig.accept(builder);
        this.gson = builder.create();
    }

    /**
     * Tests the given object using the adapter for the given class.
     *
     * @param typeClass The class of the object under test
     * @param original  The original object
     * @param <T>       The type of the object under test
     * @see #test(TypeAdapter, Object)
     */
    protected <T> void test(Class<T> typeClass, T original) {
        test(gson.getAdapter(typeClass), original);
    }

    /**
     * Tests the given adapter using the given object.
     *
     * <p>This method performs assertions based on the given adapter and object, and asserts that:</p>
     * <ol>
     *     <li>The adapter {@linkplain Assertions#assertDoesNotThrow(ThrowingSupplier) does not throw an exception}
     *     while serializing the original object.</li>
     *     <li>The adapter does not throw an exception while deserializing the resulting JSON from assertion #1.</li>
     *     <li>The resulting object from assertion #2 {@linkplain Assertions#assertNotNull(Object) is not
     *     <code>null</code>}, confirming that the deserialization succeeded.</li>
     *     <li>The adapter does not throw an exception while serializing the resulting object from assertion #2.</li>
     *     <li>The adapter does not throw an exception while deserializing the resulting JSON from assertion #4.</li>
     *     <li>The resulting object from assertion #5 is not {@code null}, confirming that the deserialization
     *     succeeded.</li>
     *     <li>The original object and the resulting object from assertion #2 {@linkplain
     *     Assertions#assertEquals(Object, Object) are equal}.</li>
     *     <li>The resulting objects from assertions #2 and #5 are equal.</li>
     *     <li>The original object and the resulting object from assertion #2 {@linkplain
     *     Assertions#assertNotSame(Object, Object) are not the same object}.</li>
     *     <li>The resulting objects from assertions #2 and #5 are not the same
     *     object.</li>
     *     <li>The resulting JSONs from assertions #1 and #4 are equal.</li>
     * </ol>
     *
     * @param adapter  The adapter for the object under test
     * @param original The original object
     * @param <T>      The type of the object under test
     */
    protected <T> void test(TypeAdapter<T> adapter, T original) {
        final String originalJson = assertDoesNotThrow(() -> adapter.toJson(original));

        final T versionA = assertDoesNotThrow(() -> adapter.fromJson(originalJson));
        assertNotNull(versionA);

        final String versionAJson = assertDoesNotThrow(() -> adapter.toJson(versionA));

        final T versionB = assertDoesNotThrow(() -> adapter.fromJson(versionAJson));
        assertNotNull(versionB);

        assertEquals(original, versionA);
        assertEquals(versionA, versionB);

        assertNotSame(original, versionA);
        assertNotSame(versionA, versionB);

        assertEquals(originalJson, versionAJson);
    }
}
