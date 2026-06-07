package io.github.lengors.scoutdesk.domain.errors;

import org.springframework.http.HttpStatusCode;

import java.util.List;

/**
 * Represents a report of constraint validation errors.
 * <p>
 * This class implements the {@link ErrorReport} interface to provide a structured way of reporting multiple constraint
 * violations that occurred in a given context.
 *
 * @param statusCode the HTTP status code associated with the validation errors
 * @param body       the list of constraints errors containing details about the violations
 * @author lengors
 */
public record ConstraintErrorReport(
  HttpStatusCode statusCode,
  List<ConstraintError> body
) implements ErrorReport {
}
