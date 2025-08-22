package io.github.lengors.scoutdesk.domain.core;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Represents a result of an operation that can either succeed with a value of type {@code T} or fail with an error of
 * type {@code E}.
 * <p>
 * This class is a sealed abstract class that defines two concrete subclasses: {@link Success} for successful results
 * and {@link Failure} for failed results.
 * <p>
 * It provides methods to handle both success and failure cases, allowing for functional-style programming patterns such
 * as mapping, flat-mapping, and recovering from errors.
 * <p>
 * The class also provides utility methods to create instances of {@link Success} and {@link Failure}, as well as
 * methods to convert the result into an {@link Optional} or a {@link Stream}.
 *
 * @param <T> the type of the value in a successful result
 * @param <E> the type of the error in a failed result
 * @author lengors
 */
public abstract sealed class Result<T, E extends Throwable> {
  /**
   * Represents a successful result containing a value of type {@code T}.
   * <p>
   * This class is a concrete subclass of {@link Result} and is used to indicate that an operation has completed
   * successfully. It contains a single field, {@code value}, which holds the successful result.
   * <p>
   * Instances of this class can be created using the static factory method {@link Result#success(Object)}.
   * <p>
   * The class also provides methods to access the value, check for success, and perform operations on the value.
   * <p>
   * The class is generic, allowing it to be used with any type of value {@code T} and any type of error {@code E}.
   *
   * @param <T> the type of the value in a successful result
   * @param <E> the type of the error in a failed result
   * @author lengors
   */
  public static final class Success<T, E extends Throwable> extends Result<T, E> {
    private final T value;

    private Success(final T value) {
      this.value = value;
    }
  }

  /**
   * Represents a failed result containing an error of type {@code E}.
   * <p>
   * This class is a concrete subclass of {@link Result} and is used to indicate that an operation has failed. It
   * contains a single field, {@code error}, which holds the error that occurred during the operation.
   * <p>
   * Instances of this class can be created using the static factory method {@link Result#failure(Throwable)}.
   * <p>
   * The class also provides methods to access the error, check for failure, and perform operations on the error.
   * <p>
   * The class is generic, allowing it to be used with any type of value {@code T} and any type of error {@code E}.
   *
   * @param <T> the type of the value in a successful result
   * @param <E> the type of the error in a failed result
   * @author lengors
   */
  public static final class Failure<T, E extends Throwable> extends Result<T, E> {
    private final E error;

    private Failure(final E error) {
      this.error = error;
    }
  }

  /**
   * Creates a new {@link Result} instance representing a failed result with the given error.
   *
   * @param error the error of the failed result
   * @param <T>   the type of the value in the successful result
   * @param <E>   the type of the error in the failed result
   * @return a new {@link Failure} instance containing the given error
   */
  public static <T, E extends Throwable> Failure<T, E> failure(final E error) {
    return new Failure<>(error);
  }

  /**
   * Creates a new {@link Result} instance representing a successful result with the given value.
   *
   * @param value the value of the successful result
   * @param <T>   the type of the value in the successful result
   * @param <E>   the type of the error in the failed result
   * @return a new {@link Success} instance containing the given value
   */
  public static <T, E extends Throwable> Success<T, E> success(final T value) {
    return new Success<>(value);
  }

  /**
   * Wraps a supplier in a {@link Result}, returning a successful result if the supplier executes without throwing an
   * exception, or a failed result if an exception is thrown.
   *
   * @param supplier the supplier to wrap
   * @param <T>      the type of the value returned by the supplier
   * @return a {@link Result} containing either the value from the supplier or the exception thrown by the supplier
   */
  public static <T> Result<T, ? extends RuntimeException> wrap(final Supplier<T> supplier) {
    try {
      return success(supplier.get());
    } catch (final RuntimeException exception) {
      return failure(exception);
    }
  }

  @Override
  public final boolean equals(final @Nullable Object object) {
    if (object == null) {
      return false;
    }
    return switch (this) {
      case Success<T, ?> thisSuccess ->
        object instanceof Success<?, ?> resultSuccess && Objects.equals(thisSuccess.value, resultSuccess.value);

      case Failure<?, E> thisFailure ->
        object instanceof Failure<?, ?> resultFailure && Objects.equals(thisFailure.error, resultFailure.error);
    };
  }

  /**
   * Applies a mapping function to the value of this result if it is successful, returning a new result with the mapped
   * value. If this result is a failure, it returns the same failure.
   *
   * @param mapper the function to apply to the value of this result if it is successful
   * @param <U>    the type of the value in the resulting successful result
   * @return a new {@link Result} containing the mapped value if this result is successful, or the same failure if this
   * result is a failure
   */
  public final <U> Result<U, ? super E> flatMap(final Function<? super T, ? extends Result<U, ? super E>> mapper) {
    return switch (this) {
      case Success<T, ?> success -> mapper.apply(success.value);
      case Failure<?, E> failure -> new Failure<>(failure.error);
    };
  }

  /**
   * Returns the value of this result if it is successful, or throws an exception if it is a failure.
   *
   * @return the value of this result if it is successful
   * @throws RuntimeException if this result is a failure
   */
  public final T get() {
    return switch (this) {
      case Success<T, ?> success -> success.value;
      case Failure<?, E> failure -> throw new RuntimeException(failure.error);
    };
  }

  @Override
  public final int hashCode() {
    return switch (this) {
      case Success<T, ?> success -> Objects.hash(success.value);
      case Failure<?, E> failure -> Objects.hash(failure.error);
    };
  }

  /**
   * Executes the given action if this result is a failure, passing the error to the action.
   *
   * @param action the action to execute if this result is a failure
   */
  public final void ifFailed(final Consumer<? super E> action) {
    if (this instanceof Failure<?, E> failure) {
      action.accept(failure.error);
    }
  }

  /**
   * Executes the given action if this result is a success, passing the value to the action.
   *
   * @param action the action to execute if this result is a success
   */
  public final void ifSuccessful(final Consumer<? super T> action) {
    if (this instanceof Success<T, ?> success) {
      action.accept(success.value);
    }
  }

  /**
   * Executes the given actions based on whether this result is a success or a failure.
   * <p>
   * If this result is a success, the {@code successAction} is executed with the value of this result. If this result is
   * a failure, the {@code failureAction} is executed with the error of this result.
   *
   * @param successAction the action to execute if this result is a success
   * @param failureAction the action to execute if this result is a failure
   */
  public final void ifSuccessfulOrElse(
    final Consumer<? super T> successAction,
    final Consumer<? super E> failureAction
  ) {
    switch (this) {
      case Success<T, ?> success -> successAction.accept(success.value);
      case Failure<?, E> failure -> failureAction.accept(failure.error);
    }
  }

  /**
   * Maps the value of this result to a new result using the given mapping function if this result is a success. If this
   * result is a failure, it returns the same failure.
   *
   * @param mapper the function to apply to the value of this result if it is a success
   * @param <U>    the type of the value in the resulting successful result
   * @return a new {@link Result} containing the mapped value if this result is successful, or the same failure if this
   * result is a failure
   */
  public final <U> Result<U, E> map(final Function<T, U> mapper) {
    return switch (this) {
      case Success<T, ?> success -> new Success<>(mapper.apply(success.value));
      case Failure<?, E> failure -> new Failure<>(failure.error);
    };
  }

  /**
   * Maps the value of this result to a new result using the given mapping function if this result is a success. If this
   * result is a failure, it returns a new result created by applying the recovery function to the error.
   *
   * @param recovery the function to apply to the error of this result if it is a failure
   * @param <X>      the type of the error in the resulting failed result
   * @return a new {@link Result} containing the mapped value if this result is successful, or a new failure if this
   * result is a failure
   */
  public final <X extends Throwable> Result<? super T, X> or(
    final Function<? super E, ? extends Result<? super T, X>> recovery
  ) {
    return switch (this) {
      case Success<T, ?> success -> new Success<>(success.value);
      case Failure<?, E> failure -> recovery.apply(failure.error);
    };
  }

  /**
   * Returns the value of this result if it is a success, or the given default value if it is a failure.
   * <p>
   * This method is useful for providing a fallback value when the operation represented by this result fails.
   * <p>
   * If this result is a success, the value is returned. If this result is a failure, the provided default value is
   * returned instead.
   *
   * @param other the default value to return if this result is a failure
   * @return the value of this result if it is a success, or the provided default value if it is a failure
   */
  public final T orElse(final T other) {
    return switch (this) {
      case Success<T, ?> success -> success.value;
      case Failure<?, E> ignored -> other;
    };
  }

  /**
   * Returns the value of this result if it is a success, or applies the given mapping function to the error and returns
   * the result.
   * <p>
   * This method is useful for providing a fallback value based on the error when the operation represented by this
   * result fails.
   *
   * @param mapper the function to apply to the error if this result is a failure
   * @return the value of this result if it is a success, or the result of applying the mapping function to the error if
   * it is a failure
   */
  public final T orElseGet(final Function<E, T> mapper) {
    return switch (this) {
      case Success<T, ?> success -> success.value;
      case Failure<?, E> failure -> mapper.apply(failure.error);
    };
  }

  /**
   * Returns the value of this result if it is a success, or throws the error if it is a failure.
   * <p>
   * This method is useful for propagating errors when the operation represented by this result fails.
   *
   * @return the value of this result if it is a success
   * @throws E the error if this result is a failure
   */
  public final T orElseThrow() throws E {
    return switch (this) {
      case Success<T, ?> success -> success.value;
      case Failure<?, E> failure -> throw failure.error;
    };
  }

  /**
   * Returns the value of this result if it is a success, or throws an exception created by the given mapping function
   * if it is a failure.
   * <p>
   * This method is useful for propagating errors with a custom exception type when the operation represented by this
   * result fails.
   *
   * @param mapper the function to apply to the error to create an exception if this result is a failure
   * @param <X>    the type of the exception to throw if this result is a failure
   * @return the value of this result if it is a success
   * @throws X the exception created by applying the mapping function to the error if this result is a failure
   */
  public final <X extends Throwable> T orElseThrow(final Function<E, X> mapper) throws X {
    return switch (this) {
      case Success<T, ?> success -> success.value;
      case Failure<?, E> failure -> throw mapper.apply(failure.error);
    };
  }

  /**
   * Converts this result into an {@link Optional} containing the value if this result is a success, or an empty
   * {@link Optional} if this result is a failure.
   * <p>
   * This method is useful for integrating with APIs that expect an {@link Optional} type.
   *
   * @return an {@link Optional} containing the value if this result is a success, or an empty {@link Optional} if this
   * result is a failure
   */
  public final Optional<@NonNull T> toOptional() {
    return switch (this) {
      case Success<T, ?> success -> Optional.ofNullable(success.value);
      case Failure<?, E> ignored -> Optional.empty();
    };
  }

  /**
   * Converts this result into a {@link Stream} containing the value if this result is a success, or an empty stream if
   * this result is a failure.
   * <p>
   * This method is useful for integrating with APIs that expect a {@link Stream} type.
   *
   * @return a {@link Stream} containing the value if this result is a success, or an empty stream if this result is a
   * failure
   */
  public final Stream<T> stream() {
    return switch (this) {
      case Success<T, ?> success -> Stream.of(success.value);
      case Failure<?, E> ignored -> Stream.empty();
    };
  }

  @Override
  public final String toString() {
    return switch (this) {
      case Success<T, ?> success -> "Success(value=%s)".formatted(Objects.requireNonNullElse(success.value, "null"));
      case Failure<?, E> failure -> "Failure(error=%s)".formatted(failure.error);
    };
  }
}
