package io.github.lengors.scoutdesk.domain.errors;

import org.springframework.http.HttpStatusCode;

/**
 * Represents an error report containing an HTTP status code and a message body.
 * <p>
 * This record serves as an implementation of the {@link ErrorReport} interface. It encapsulates the HTTP status code
 * representing the type of error and a string message providing additional details about the error.
 * <p>
 * Typical use cases involve scenarios where errors need to be reported in a structured format, such as mapping
 * exceptions to HTTP responses.
 *
 * @param statusCode the HTTP status code describing the category or type of the error
 * @param body       the string representation of the error message or details
 * @author lengors
 */
public record MessageErrorReport(
  HttpStatusCode statusCode,
  String body
) implements ErrorReport {
}
