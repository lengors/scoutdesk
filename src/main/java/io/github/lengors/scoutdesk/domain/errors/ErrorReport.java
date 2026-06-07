package io.github.lengors.scoutdesk.domain.errors;

import org.springframework.http.HttpStatusCode;

/**
 * Represents a general structure for error reporting. The implementations of this interface should provide a mechanism
 * for encapsulating error details, such as the HTTP status code and the body describing the error.
 *
 * @author lengors
 */
public interface ErrorReport {

  /**
   * Retrieves the body of the error report, providing specific details of the error.
   *
   * @return an object representing the detailed information about the error, which can vary depending on the
   * implementation.
   */
  Object body();

  /**
   * Retrieves the HTTP status code associated with the error report.
   *
   * @return the HTTP status code, indicating the type or category of the error, as defined by the implementation.
   */
  HttpStatusCode statusCode();
}
