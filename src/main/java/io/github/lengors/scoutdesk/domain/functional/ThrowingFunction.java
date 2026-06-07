package io.github.lengors.scoutdesk.domain.functional;

/**
 * Represents a functional interface similar to {@link java.util.function.Function}, but allows the functional method to
 * throw a checked exception.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @param <E> the type of the exception that the function may throw
 * @author lengors
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> {

  /**
   * Applies this function to the given input and produces a result, potentially throwing an exception of type E.
   *
   * @param input the input to the function
   * @return the result of the function application
   * @throws E if an exception occurs during function execution
   */
  R apply(T input) throws E;
}
