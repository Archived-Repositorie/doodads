package io.github.justfoxx.doodads;

import java.util.Objects;

public sealed interface Option<T> extends Copy<Option<T>> {
    @SuppressWarnings("unchecked")
    static <T> None<T> None() {
        return (None<T>) None.INSTANCE;
    }
    record Some<T>(T value) implements Option<T> {
        public Some {
            Objects.requireNonNull(value);
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Some(Object someValue))) return false;
            return Objects.equals(value, someValue);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return "Some{" +
                    "value=" + value +
                    '}';
        }

        @Override
        public Option<T> copy() {
            return new Some<>(value);
        }
    }

    final class None<T> implements Option<T> {
        private None() {}
        private static final None<?> INSTANCE = new None<>();

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public String toString() {
            return "None{}";
        }

        @Override
        public Option<T> copy() {
            return this;
        }
    }
}
