/*
 * Copyright 2019 Feather Core
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.feathercore.shared.object;

import lombok.*;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * A value which may be present (which includes {@link null}) or not-present.
 * <p>
 * This differs from {@link java.util.Optional<T>} as presence of a {@link null} value
 * does not make this value container empty.
 */
public interface ValueContainer<T> extends Supplier<T> {

    /**
     * Gets a value container being empty.
     *
     * @param <T> type of value container
     * @return empty value container
     */
    @SuppressWarnings("unchecked")
    static <T> ValueContainer<T> empty() {
        return (ValueContainer<T>) Empty.INSTANCE;
    }

    /**
     * Gets a non-empty value container containing {@link null}.
     *
     * @param <T> type of value container
     * @return value container containing {@link null}
     */
    @SuppressWarnings("unchecked")
    static <T> ValueContainer<T> ofNull() {
        return (ValueContainer<T>) Empty.INSTANCE;
    }

    /**
     * Gets a non-empty value container containing the specified value.
     *
     * @param <T> type of value container
     * @return non-empty value container containing the specified value
     */
    @SuppressWarnings("unchecked")
    static <T> ValueContainer<T> of(@Nullable final T value) {
        return value == null ? (ValueContainer<T>) OfNull.INSTANCE : new Containing<>(value);
    }

    /**
     * Gets a value container containing the specified value or an empty one if the values is {@link null}.
     *
     * @param <T> type of value container
     * @return non-empty value container containing the specified value
     * if it is not {@link null} and an empty one otherwise
     */
    @SuppressWarnings("unchecked")
    static <T> ValueContainer<T> nonnullOrEmpty(@Nullable final T value) {
        return value == null ? (ValueContainer<T>) Empty.INSTANCE : new Containing<>(value);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @throws EmptyValueException if this value container is empty
     */
    @Override
    T get() throws EmptyValueException;

    /**
     * Checks whether the object is present ot not.
     *
     * @return {@link true} if the value is present (might be {@link null}) and {@link false} otherwise
     */
    boolean isPresent();

    /**
     * Gets the value if it is present otherwise using the specified one.
     *
     * @param value value to use if this value container is empty
     * @return this value container's value if it is not empty or the specified value otherwise
     */
    @Nullable default T orElse(@Nullable final T value) {
        return isPresent() ? get() : value;
    }

    /**
     * Gets the value if it is present otherwise using the one got from the specified supplier.
     *
     * @param valueSupplier supplier to be used for getting the value if this value container is empty
     * @return this value container's value if it is not empty or the one got from the supplier otherwise
     */
    @Nullable default T orElseGet(@NonNull final Supplier<T> valueSupplier) {
        return isPresent() ? get() : valueSupplier.get();
    }

    /**
     * Gets the value if it is present otherwise throwing an exception.
     *
     * @param exceptionSupplier supplier to be used for getting the exception if this value container is empty
     * @param <X> type of an exception thrown if this value container is empty
     * @return this value container's value if it is not empty
     * @throws X if this value container is empty
     */
    @Nullable default <X extends Throwable> T orElseThrow(@NonNull final Supplier<@NotNull X> exceptionSupplier)
            throws X {
        if (isPresent()) {
            return get();
        }

        throw exceptionSupplier.get();
    }

    // use default equals and hashCode
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    final class Empty<T> implements ValueContainer<T> {

        private static final Empty<?> INSTANCE = new Empty<>();

        @Override
        public T get() throws EmptyValueException {
            throw new EmptyValueException("There is no value associated with this value container");
        }

        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public String toString() {
            return "Empty ValueContainer";
        }
    }

    // use default equals and hashCode
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    final class OfNull<T> implements ValueContainer<T> {

        private static final OfNull<?> INSTANCE = new OfNull<>();

        @Override
        public T get() throws EmptyValueException {
            return null;
        }

        @Override
        public boolean isPresent() {
            return true;
        }

        @Override
        public String toString() {
            return "ValueContainer{null}";
        }
    }

    @Value
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    class Containing<T> implements ValueContainer<T> {

        @NonNull private final T value;

        @Override
        public T get() throws EmptyValueException {
            return value;
        }

        @Override
        public boolean isPresent() {
            return true;
        }
    }

    @Value
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    class Supplying<T> implements ValueContainer<T> {

        @NonNull @Delegate private final Supplier<T> valueSupplier;

        @Override
        public boolean isPresent() {
            return true;
        }
    }

    /**
     * An exception thrown whenever {@link #get()} is called on an empty value container.
     */
    @NoArgsConstructor
    class EmptyValueException extends RuntimeException {
        //<editor-fold desc="Inheriting constructors" defaultstate="collapsed">
        public EmptyValueException(final String message) {
            super(message);
        }

        public EmptyValueException(final String message, final Throwable cause) {
            super(message, cause);
        }

        public EmptyValueException(final Throwable cause) {
            super(cause);
        }

        public EmptyValueException(final String message, final Throwable cause, final boolean enableSuppression,
                                   final boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
        //</editor-fold>
    }
}
