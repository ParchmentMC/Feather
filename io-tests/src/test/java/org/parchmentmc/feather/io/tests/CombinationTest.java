package org.parchmentmc.feather.io.tests;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CombinationTest {

    public void test(List<Pair<Integer, Integer>> expected, Collection<Integer> input) {
        test(expected, input.iterator());
    }

    public void test(List<Pair<Integer, Integer>> expected, Iterator<Integer> input) {
        List<Pair<Integer, Integer>> actual = Combinations.pairs(input);

        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
        assertNotSame(expected, actual);
    }

    @Test
    public void testEmpty() {
        test(
            Collections.emptyList(),
            Collections.emptyIterator()
        );
    }

    @Test
    public void testSingle() {
        test(
            Collections.emptyList(),
            Collections.singleton(0)
        );
    }

    @Test
    public void testPair() {
        test(
            Lists.newArrayList(Pair.of(0, 1)),
            Lists.newArrayList(0, 1)
        );
    }

    @Test
    public void testTriple() {
        test(
            Lists.newArrayList(Pair.of(0, 1), Pair.of(0, 2), Pair.of(1, 2)),
            Lists.newArrayList(0, 1, 2)
        );
    }

    @Test
    public void testQuartet() {
        test(
            Lists.newArrayList(Pair.of(0, 1), Pair.of(0, 2), Pair.of(0, 3), Pair.of(1, 2), Pair.of(1, 3), Pair.of(2, 3)),
            Lists.newArrayList(0, 1, 2, 3)
        );
    }



}
