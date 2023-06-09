package org.parchmentmc.feather.util;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class CollectorUtils {

    private CollectorUtils() {
        throw new IllegalStateException("Can not instantiate an instance of: CollectorUtils. This is a utility class");
    }

    /**
     * Returns a {@code Collector} that accumulates elements into a
     * {@code Map} whose keys and values are the result of applying the provided
     * mapping functions to the input elements, and which retains the insertion order of
     * accumulated elements.
     *
     * <p>If the mapped keys contains duplicates (according to
     * {@link Object#equals(Object)}), an {@code IllegalStateException} is
     * thrown when the collection operation is performed.  If the mapped keys
     * may have duplicates, use {@link Collectors#toMap(Function, Function, BinaryOperator)}
     * instead.
     *
     * <p>This method behaves equivalently to {@link Collectors#toMap(Function, Function)}
     * with the notable difference that the returned {@link Map} retains the insertion order
     * of accumulated elements.
     *
     * @param <T> the type of the input elements
     * @param <K> the output type of the key mapping function
     * @param <U> the output type of the value mapping function
     * @param keyMapper a mapping function to produce keys
     * @param valueMapper a mapping function to produce values
     * @return a {@code Collector} which collects elements into a {@code Map}
     * whose keys and values are the result of applying mapping functions to
     * the input elements, and which retains the insertion order of
     * accumulated elements
     *
     * @see #toLinkedMap(Function, Function, BinaryOperator)
     * @see Collectors#toMap(Function, Function)
     */
    public static <T, K, U> Collector<T, ?, Map<K,U>> toLinkedMap(Function<? super T, ? extends K> keyMapper,
                                                                  Function<? super T, ? extends U> valueMapper) {
        return Collectors.toMap(keyMapper, valueMapper, throwingMerger(), LinkedHashMap::new);
    }

    /**
     * Returns a {@code Collector} that accumulates elements into a
     * {@code Map} whose keys and values are the result of applying the provided
     * mapping functions to the input elements, and which retains the insertion order of
     * accumulated elements.
     *
     * <p>If the mapped
     * keys contains duplicates (according to {@link Object#equals(Object)}),
     * the value mapping function is applied to each equal element, and the
     * results are merged using the provided merging function.
     *
     * <p>This method behaves equivalently to {@link Collectors#toMap(Function, Function, BinaryOperator)}
     * with the notable difference that the returned {@link Map} retains the insertion order
     * of accumulated elements.
     *
     * @param <T> the type of the input elements
     * @param <K> the output type of the key mapping function
     * @param <U> the output type of the value mapping function
     * @param keyMapper a mapping function to produce keys
     * @param valueMapper a mapping function to produce values
     * @param mergeFunction a merge function, used to resolve collisions between
     *                      values associated with the same key, as supplied
     *                      to {@link Map#merge(Object, Object, BiFunction)}
     * @return a {@code Collector} which collects elements into a {@code Map}
     * whose keys are the result of applying a key mapping function to the input
     * elements, and whose values are the result of applying a value mapping
     * function to all input elements equal to the key and combining them
     * using the merge function, and which retains the insertion order of
     * accumulated elements
     *
     * @see Collectors#toMap(Function, Function, BinaryOperator)
     */
    public static <T, K, U>
    Collector<T, ?, Map<K,U>> toLinkedMap(Function<? super T, ? extends K> keyMapper,
                                    Function<? super T, ? extends U> valueMapper,
                                    BinaryOperator<U> mergeFunction) {
        return Collectors.toMap(keyMapper, valueMapper, mergeFunction, LinkedHashMap::new);
    }

    /**
     * Returns a merge function, suitable for use in
     * {@link Map#merge(Object, Object, BiFunction) Map.merge()} or
     * {@link Collectors#toMap(Function, Function, BinaryOperator) toMap()}, which always
     * throws {@code IllegalStateException}.  This can be used to enforce the
     * assumption that the elements being collected are distinct.
     *
     * @param <T> the type of input arguments to the merge function
     * @return a merge function which always throw {@code IllegalStateException}
     */
    private static <T> BinaryOperator<T> throwingMerger() {
        return (u,v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); };
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code Set}. There are no guarantees on the type, mutability,
     * serializability, or thread-safety of the {@code Set} returned, however insertion
     * order is maintained.
     *
     * @param <T> the type of the input elements
     * @return a {@code Collector} which collects all the input elements into a
     * {@code Set} maintaining insertion order
     */
    public static <T> Collector<T, ?, LinkedHashSet<T>> toLinkedSet() {
        return Collectors.toCollection(LinkedHashSet::new);
    }
}
