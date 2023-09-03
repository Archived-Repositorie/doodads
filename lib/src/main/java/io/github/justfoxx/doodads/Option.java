package io.github.justfoxx.doodads;

import jakarta.annotation.Nullable;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public sealed interface Option<TValue> {
    static <TValue> Option.Some<TValue> some(TValue value) {
        return new Some<>(value);
    }

    @SuppressWarnings("unchecked")
    static <TValue> Option.None<TValue> none() {
        return (Option.None<TValue>) None.INSTANCE;
    }

    static <TValue> Option<TValue> from(@Nullable TValue value) {
        return value == null ? none() : some(value);
    }

    boolean isSome();

    default boolean isNone() {
        return !isSome();
    }

    TValue getOrDefault(TValue value);

    TValue getOrThrow() throws NoSuchElementException;

    <X extends Throwable> TValue getOrThrow(Supplier<? extends X> exceptionSupplier) throws X;

    void ifPresent(Consumer<TValue> consumer);

    void ifPresentOrElse(Consumer<TValue> consumer, Runnable runnable);

    Type getType();

    Option<TValue> filter(Predicate<TValue> predicate);

    <TMapped> Option<TMapped> map(Function<TValue, TMapped> mapper);

    <TMapped> Option<TMapped> flatMap(Function<TValue, Option<TMapped>> mapper);

    Option<TValue> or(Supplier<Option<TValue>> other);

    Stream<TValue> stream();

    enum Type {
        SOME,
        NONE
    }

    final class Some<TValue> implements Option<TValue> {
        private final TValue value;

        private Some(TValue value) {
            this.value = value;
        }

        public TValue get() {
            return value;
        }

        @Override
        public boolean isSome() {
            return true;
        }

        @Override
        public TValue getOrDefault(TValue value) {
            return get();
        }

        @Override
        public TValue getOrThrow() {
            return get();
        }

        @Override
        public <X extends Throwable> TValue getOrThrow(Supplier<? extends X> exceptionSupplier) {
            return get();
        }

        @Override
        public void ifPresent(Consumer<TValue> consumer) {
            consumer.accept(get());
        }

        @Override
        public void ifPresentOrElse(Consumer<TValue> consumer, Runnable runnable) {
            consumer.accept(get());
        }

        @Override
        public Type getType() {
            return Type.SOME;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Some<?> some && value.equals(some.value);
        }

        @Override
        public String toString() {
            return String.format("Option.Some(%s)", value);
        }

        @Override
        public Option<TValue> filter(Predicate<TValue> predicate) {
            return predicate.test(value) ? this : none();
        }

        @Override
        public <TMapped> Option<TMapped> map(Function<TValue, TMapped> mapper) {
            return some(mapper.apply(value));
        }

        @Override
        public <TMapped> Option<TMapped> flatMap(Function<TValue, Option<TMapped>> mapper) {
            return mapper.apply(value);
        }

        @Override
        public Option<TValue> or(Supplier<Option<TValue>> other) {
            return this;
        }

        @Override
        public Stream<TValue> stream() {
            return Stream.of(value);
        }
    }

    final class None<TValue> implements Option<TValue> {
        private static final None<?> INSTANCE = new None<>();

        private None() {}

        @Override
        public boolean isSome() {
            return false;
        }

        @Override
        public TValue getOrDefault(TValue value) {
            return value;
        }

        @Override
        public TValue getOrThrow() throws NoSuchElementException {
            throw new NoSuchElementException("No value present");
        }

        @Override
        public <X extends Throwable> TValue getOrThrow(Supplier<? extends X> exceptionSupplier) throws X {
            throw exceptionSupplier.get();
        }

        @Override
        public void ifPresent(Consumer<TValue> consumer) {}

        @Override
        public void ifPresentOrElse(Consumer<TValue> consumer, Runnable runnable) {
            runnable.run();
        }

        @Override
        public Type getType() {
            return Type.NONE;
        }

        @Override
        public Option<TValue> filter(Predicate<TValue> predicate) {
            return this;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof None;
        }

        @Override
        public String toString() {
            return "Option.None";
        }

        @Override
        public <TMapped> Option<TMapped> map(Function<TValue, TMapped> mapper) {
            return none();
        }

        @Override
        public <TMapped> Option<TMapped> flatMap(Function<TValue, Option<TMapped>> mapper) {
            return none();
        }

        @Override
        public Option<TValue> or(Supplier<Option<TValue>> other) {
            return other.get();
        }

        @Override
        public Stream<TValue> stream() {
            return Stream.empty();
        }
    }
}
