package io.github.lengors.scoutdesk.domain.io.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Thrown to indicate that the data being processed is in an invalid or unexpected format. This exception is a specific
 * subtype of {@code LoadDataException}, providing a context for errors where the format of the data causes processing
 * failures.
 * <p>
 * It can be instantiated with an optional {@link JsonProcessingException} or {@link IllegalArgumentException} as the
 * cause, or without a specific cause.
 *
 * @author lengors
 */
public final class LoadDataFormatException extends LoadDataException {

  /**
   * Contains the default error message for exceptions related to invalid data format encountered during input
   * processing. This message is used to describe the nature of the issue when an unexpected or improper data format is
   * detected.
   */
  public static final String MESSAGE = "Invalid data format while processing input";

  private LoadDataFormatException(
    final Object source,
    final @Nullable Throwable cause
  ) {
    super(source, MESSAGE, cause);
  }

  /**
   * Constructs a new {@code LoadDataFormatException} with the specified data source and {@code JsonProcessingException}
   * as the cause. This constructor is used when a JSON processing error occurs, indicating an invalid or unexpected
   * data format during the loading process.
   *
   * @param source the source related to the cause of the exception; can represent the data being processed or its
   *               origin.
   * @param cause  the underlying {@code JsonProcessingException} that caused this exception.
   */
  public LoadDataFormatException(
    final Object source,
    final JsonProcessingException cause
  ) {
    this(source, (Throwable) cause);
  }

  /**
   * Constructs a new {@code LoadDataFormatException} with the specified data source and
   * {@code IllegalArgumentException} as the cause. This constructor is used when an illegal argument error occurs,
   * indicating an invalid or unexpected data format during the loading process.
   *
   * @param source the source related to the cause of the exception; can represent the data being processed or its
   *               origin.
   * @param cause  the underlying {@code IllegalArgumentException} that caused this exception.
   */
  public LoadDataFormatException(
    final Object source,
    final IllegalArgumentException cause
  ) {
    this(source, (Throwable) cause);
  }

  /**
   * Constructs a new {@code LoadDataFormatException} with the specified data source. This constructor is used when an
   * invalid or unexpected data format is encountered during the loading process, but no specific cause is provided.
   *
   * @param source the source related to the cause of the exception; can represent the data being processed or its
   *               origin.
   */
  public LoadDataFormatException(final Object source) {
    this(source, (@Nullable Throwable) null);
  }
}
