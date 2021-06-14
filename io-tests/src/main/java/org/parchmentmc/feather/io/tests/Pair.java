package org.parchmentmc.feather.io.tests;

import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;

public class Pair<A, B> {

    private final A a;
    private final B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<>(a, b);
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> that = (Pair<?, ?>) o;
        return getA() == that.getA() && getB().equals(that.getB());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getA(), getB());
    }

}
