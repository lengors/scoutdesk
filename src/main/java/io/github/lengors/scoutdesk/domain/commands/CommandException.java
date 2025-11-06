package io.github.lengors.scoutdesk.domain.commands;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Supplier;

/**
 * Exception that wraps a command request and an optional underlying cause. Used to propagate command execution errors
 * while retaining context about the command that failed. The underlying cause can be another exception that triggered
 * the command failure. Provides methods to unwrap nested CommandExceptions and retrieve the original cause. Also
 * includes a utility method to execute a supplier and unwrap any CommandException thrown, rethrowing the underlying
 * cause if present.
 *
 * @author lengors
 */
public final class CommandException extends RuntimeException {
  private final CommandRequest<?, ?, ?> commandRequest;

  /**
   * Creates a new CommandException with the given command request and optional underlying cause.
   *
   * @param commandRequest the command request that caused the exception
   * @param cause          the underlying cause of the exception, may be null
   */
  public CommandException(
    final CommandRequest<?, ?, ?> commandRequest,
    final @Nullable RuntimeException cause
  ) {
    super(cause);
    this.commandRequest = commandRequest;
  }

  /**
   * Creates a new CommandException with the given command request and no underlying cause.
   *
   * @param commandRequest the command request that caused the exception
   */
  public CommandException(final CommandRequest<?, ?, ?> commandRequest) {
    this(commandRequest, null);
  }

  /**
   * Gets the command request that caused this exception.
   *
   * @return the command request
   */
  public CommandRequest<?, ?, ?> getCommandRequest() {
    return commandRequest;
  }

  /**
   * Unwraps nested CommandExceptions to retrieve the deepest underlying CommandException.
   *
   * @return the deepest nested CommandException
   */
  public CommandException getNestedCause() {
    CommandException commandException = this;
    while (commandException.getCause() instanceof CommandException nextCommandException) {
      commandException = nextCommandException;
    }
    return commandException;
  }

  /**
   * Overrides getCause to return a RuntimeException, which is the expected type of underlying causes.
   *
   * @return the underlying cause as a RuntimeException, or null if none
   */
  @Override
  public synchronized @Nullable RuntimeException getCause() {
    return (@Nullable RuntimeException) super.getCause();
  }

  /**
   * Retrieves the original underlying cause by unwrapping nested CommandExceptions.
   *
   * @return the original underlying RuntimeException cause, or null if none
   */
  public @Nullable RuntimeException getUnderlyingCause() {
    return getNestedCause().getCause();
  }

  /**
   * Executes the given supplier and unwraps any CommandException thrown, rethrowing the underlying cause if present.
   *
   * @param supplier the supplier to execute
   * @param <T>      the type of result supplied
   * @return the result from the supplier, or null if a CommandException was thrown without an underlying cause
   * @throws RuntimeException if a CommandException was thrown with an underlying cause
   */
  public static <T> @Nullable T unwrap(final Supplier<T> supplier) {
    try {
      return supplier.get();
    } catch (final CommandException commandException) {
      final var underlyingCause = commandException.getUnderlyingCause();
      if (underlyingCause != null) {
        throw underlyingCause;
      }
    }
    return null;
  }
}
