package org.parchmentmc.feather.io.moshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Abstract superclass for tests which use Moshi functionality (such as de/serialization tests).
 */
public abstract class MoshiTest {
    protected final Moshi moshi;

    protected MoshiTest(Consumer<Moshi.Builder> builderConfig) {
        final Moshi.Builder builder = new Moshi.Builder();
        builderConfig.accept(builder);
        this.moshi = builder.build();
    }

    /**
     * Tests the given object using the adapter for the given class.
     *
     * @param typeClass The class of the object under test
     * @param original  The original object
     * @param <T>       The type of the object under test
     * @see #test(JsonAdapter, Object)
     */
    protected <T> void test(Class<T> typeClass, T original) {
        test(moshi.adapter(typeClass), original);
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
    protected <T> void test(JsonAdapter<T> adapter, T original) {
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
