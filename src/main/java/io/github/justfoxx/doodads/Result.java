package io.github.justfoxx.doodads;

import java.util.Objects;

public sealed interface Result<T,X> extends Copy<Result<T,X>> {
    record Ok<T,X>(T value) implements Result<T,X> {
        public Ok {
            Objects.requireNonNull(value);
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Ok(Object okValue))) return false;
            return Objects.equals(value, okValue);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return "Ok{" +
                    "value=" + value +
                    '}';
        }

        @Override
        public Result<T, X> copy() {
            return new Ok<>(value);
        }
    }

    record Error<T,X>(X value) implements Result<T,X> {
        public Error {
            Objects.requireNonNull(value);
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Error(Object errorValue))) return false;
            return Objects.equals(value, errorValue);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return "Error{" +
                    "value=" + value +
                    '}';
        }

        @Override
        public Result<T, X> copy() {
            return new Error<>(value);
        }
    }
}
