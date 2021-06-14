package org.parchmentmc.feather.io.tests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class Combinations {

    public static <E> List<Pair<E, E>> pairs(Iterator<E> input) {
        List<Pair<E, E>> output = new ArrayList<>();

        pairs(input, (a, b) -> output.add(new Pair<>(a, b)));

        return output;
    }

    public static <E> void pairs(Iterator<E> input, BiConsumer<E, E> output) {
        Set<E> set = new HashSet<E>();
        input.forEachRemaining(set::add);

        Iterator<E> iterator = set.iterator();
        while (iterator.hasNext()) {
            E element = iterator.next();
            iterator.remove();

            set.forEach(other -> output.accept(element, other));
        }
    }



}
