package io.github.lengors.scoutdesk.domain.io.exceptions;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a base exception that may occur during the process of loading data. This is an abstract sealed class,
 * allowing only specific types of data load-related exceptions (e.g., {@link LoadDataFormatException},
 * {@link LoadDataIOException}) to extend it.
 * <p>
 * The class includes details about the data source that caused the exception and allows for an optional message and
 * cause.
 *
 * @author lengors
 */
public abstract sealed class LoadDataException extends RuntimeException
  permits LoadDataFormatException, LoadDataIOException {

  /**
   * Represents the source or origin of the data that is associated with an exception. This field is used to provide
   * context about the data or its origin that caused the exception. It is immutable and set at the time of exception
   * construction.
   */
  private final Object source;

  /**
   * Constructs a new {@code LoadDataException} instance with the specified data source and cause. This constructor
   * allows creating an exception related to the loading process without a specific message.
   *
   * @param source the source related to the cause of the exception; can represent the data being processed or its
   *               origin.
   * @param cause  the underlying cause of the exception, or {@code null} if no specific cause is provided.
   */
  public LoadDataException(
    final Object source,
    final @Nullable Throwable cause
  ) {
    this(source, null, cause);
  }

  /**
   * Constructs a new {@code LoadDataException} instance with the specified data source, message, and cause. This
   * constructor provides a more detailed context for the exception, including the source of the data, an optional
   * message describing the error, and an optional cause.
   *
   * @param source  the source related to the cause of the exception; can represent the data being processed or its
   *                origin.
   * @param message a detailed message providing more context about the exception, or {@code null} if no specific
   *                message is required.
   * @param cause   the underlying cause of the exception, or {@code null} if no specific cause is provided.
   */
  public LoadDataException(
    final Object source,
    final @Nullable String message,
    final @Nullable Throwable cause
  ) {
    super(message, cause);
    this.source = source;
  }

  /**
   * Retrieves the source associated with this exception. The source typically represents the data or its origin related
   * to where the exception occurred.
   *
   * @return the object representing the source of the exception.
   */
  public Object getSource() {
    return source;
  }
}
