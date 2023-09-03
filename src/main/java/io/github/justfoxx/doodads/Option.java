/*
 * Copyright (c) 2023. JustFoxx
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.justfoxx.doodads;

import jakarta.annotation.Nullable;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A sealed interface representing an Option type for handling optional values.
 *
 * @param <TValue> The type of the optional value.
 */
public sealed interface Option<TValue> {

    /**
     * Create an Option instance containing a non-null value.
     *
     * @param value The non-null value to be wrapped in an Option.
     * @param <TValue> The type of the value.
     * @return An Option instance containing the provided value.
     */
    static <TValue> Option.Some<TValue> some(TValue value) {
        return new Some<>(value);
    }

    /**
     * Create an Option instance representing the absence of a value.
     *
     * @param <TValue> The type of the absent value.
     * @return An Option instance representing the absence of a value.
     */
    @SuppressWarnings("unchecked")
    static <TValue> Option.None<TValue> none() {
        return (Option.None<TValue>) None.INSTANCE;
    }

    /**
     * Create an Option instance from a nullable value. If the value is null,
     * it returns an Option.None, otherwise, it returns an Option.Some containing
     * the non-null value.
     *
     * @param value The nullable value to be wrapped in an Option.
     * @param <TValue> The type of the value.
     * @return An Option instance containing the provided non-null value, or
     * Option.None if the value is null.
     */
    static <TValue> Option<TValue> from(@Nullable TValue value) {
        return value == null ? none() : some(value);
    }

    /**
     * Check if the Option contains a value (Option.Some).
     *
     * @return true if the Option contains a value, false otherwise.
     */
    boolean isSome();

    /**
     * Check if the Option does not contain a value (Option.None).
     *
     * @return true if the Option does not contain a value, false otherwise.
     */
    default boolean isNone() {
        return !isSome();
    }

    /**
     * Get the value contained in the Option, or a default value if the Option is None.
     *
     * @param value The default value to return if the Option is None.
     * @return The contained value if the Option is Some, or the default value if None.
     */
    TValue getOrDefault(TValue value);

    /**
     * Get the value contained in the Option, or throw a NoSuchElementException if the Option is None.
     *
     * @return The contained value if the Option is Some.
     * @throws NoSuchElementException if the Option is None.
     */
    TValue getOrThrow() throws NoSuchElementException;

    /**
     * Get the value contained in the Option, or throw a custom exception if the Option is None.
     *
     * @param exceptionSupplier A supplier for the custom exception to throw.
     * @param <X> The type of the custom exception.
     * @return The contained value if the Option is Some.
     * @throws X if the Option is None.
     */
    <X extends Throwable> TValue getOrThrow(Supplier<? extends X> exceptionSupplier) throws X;

    /**
     * Perform an action with the contained value if the Option is Some.
     *
     * @param consumer The action to perform with the contained value.
     */
    void ifPresent(Consumer<TValue> consumer);

    /**
     * Perform an action with the contained value if the Option is Some, or
     * perform another action if it is None.
     *
     * @param consumer The action to perform with the contained value.
     * @param runnable The action to perform if the Option is None.
     */
    void ifPresentOrElse(Consumer<TValue> consumer, Runnable runnable);

    /**
     * Get the type of the Option, either Some or None.
     *
     * @return The type of the Option.
     */
    Type getType();

    /**
     * Filter the Option's value based on a predicate.
     *
     * @param predicate The predicate to apply to the value.
     * @return An Option containing the value if it satisfies the predicate,
     * or Option.None if it does not.
     */
    Option<TValue> filter(Predicate<TValue> predicate);

    /**
     * Map the value inside the Option to another value using a mapper function.
     *
     * @param mapper The function to transform the value.
     * @param <TMapped> The type of the transformed value.
     * @return An Option containing the transformed value.
     */
    <TMapped> Option<TMapped> map(Function<TValue, TMapped> mapper);

    /**
     * FlatMap the value inside the Option to another Option using a mapper function.
     *
     * @param mapper The function to transform the value into another Option.
     * @param <TMapped> The type of the transformed Option's value.
     * @return The transformed Option.
     */
    <TMapped> Option<TMapped> flatMap(Function<TValue, Option<TMapped>> mapper);

    /**
     * Return this Option if it is Some, or return another Option provided by a supplier if it is None.
     *
     * @param other The supplier of another Option.
     * @return This Option if it is Some, or the Option provided by the supplier if it is None.
     */
    Option<TValue> or(Supplier<Option<TValue>> other);

    /**
     * Convert the Option to a Stream containing the value if it is Some, or an empty Stream if it is None.
     *
     * @return A Stream containing the value if it is Some, or an empty Stream if it is None.
     */
    Stream<TValue> stream();

    /**
     * Enum representing the type of the Option, either Some or None.
     */
    enum Type {
        SOME,
        NONE
    }

    /**
     * A class representing an Option containing a non-null value (Option.Some).
     *
     * @param <TValue> The type of the contained value.
     */
    final class Some<TValue> implements Option<TValue> {
        private final TValue value;

        private Some(TValue value) {
            this.value = value;
        }

        /**
         * Get the value contained in the Option.
         *
         * @return The contained value.
         */
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

    /**
     * A class representing an Option with no value (Option.None).
     *
     * @param <TValue> The type of the absent value.
     */
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
