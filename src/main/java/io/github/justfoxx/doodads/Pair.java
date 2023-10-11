package io.github.justfoxx.doodads;

import java.util.Objects;

public record Pair<A,B>(A a, B b) implements Copy<Pair<A,B>> {
    public Pair {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair(Object pairA, Object pairB))) return false;
        return Objects.equals(a, pairA) && Objects.equals(b, pairB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @Override
    public Pair<A, B> copy() {
        return new Pair<>(a, b);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}
