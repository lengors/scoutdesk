package io.github.lengors.scoutdesk.domain.io.exceptions;

import java.io.IOException;

/**
 * This exception is a specific subclass of {@link LoadDataException} that is thrown when an {@link IOException} occurs
 * during the process of loading data. It provides a predefined message indicating that input data could not be read,
 * along with the root cause of the exception.
 * <p>
 * This class is final and cannot be subclassed further.
 *
 * @author lengors
 */
public final class LoadDataIOException extends LoadDataException {
  private static final String MESSAGE = "Could not read data from input";

  /**
   * Constructs a new {@code LoadDataIOException} instance with the specified data source and cause. This exception is
   * thrown when an {@link IOException} occurs during the process of loading data.
   *
   * @param source the source related to the cause of the exception; can represent the data being processed or its
   *               origin.
   * @param cause  the {@link IOException} that triggered this exception.
   */
  public LoadDataIOException(
    final Object source,
    final IOException cause
  ) {
    super(source, MESSAGE, cause);
  }
}
