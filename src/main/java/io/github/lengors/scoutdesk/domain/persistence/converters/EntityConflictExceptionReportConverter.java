package io.github.lengors.scoutdesk.domain.persistence.converters;

import io.github.lengors.scoutdesk.domain.errors.ErrorReport;
import io.github.lengors.scoutdesk.domain.errors.MessageErrorReport;
import io.github.lengors.scoutdesk.domain.persistence.exceptions.EntityConflictException;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Converts an {@link EntityConflictException} into an {@link ErrorReport} representation.
 * <p>
 * This class is a Spring {@link Component} and implements the {@link Converter} interface, allowing it to transform
 * {@link EntityConflictException} instances into structured error reports that can be used for output or logging
 * purposes.
 * <p>
 * The conversion logic encapsulated in this class creates a {@link MessageErrorReport} that includes an HTTP status
 * code of {@link HttpStatus#CONFLICT} and a detailed error message. The error message includes the runtime type of the
 * entity and the associated query, both of which are extracted from the source {@link EntityConflictException}.
 * <p>
 * The error message is formatted using a defined string pattern: "Entity {type=%s} cannot be deleted because at least
 * one of {query=%s} depends on it".
 * <p>
 * This converter helps map technical exceptions to a user-friendly and structured error format, which can be returned
 * as part of an error response, typically in the context of a REST API.
 *
 * @author lengors
 */
@Component
public class EntityConflictExceptionReportConverter implements Converter<EntityConflictException, ErrorReport> {

  /**
   * A message template used to describe a conflict scenario where an entity cannot be deleted due to dependencies on it
   * by another entity.
   * <p>
   * The message format includes placeholders for the type of the entity and the query used to reference it: "Entity
   * {type=%s} cannot be deleted because at least one of {query=%s} depends on it".
   * <p>
   * This template is primarily utilized in the context of converting {@link EntityConflictException} into a structured
   * {@link ErrorReport}, enabling a detailed, user-friendly error message for API responses or logging.
   */
  public static final String MESSAGE =
    "Entity {type=%s} cannot be deleted because at least one of {query=%s} depends on it";

  EntityConflictExceptionReportConverter() {
    // Empty constructor for protecting against unwanted instantiation
  }

  /**
   * Converts an {@link EntityConflictException} to a {@link MessageErrorReport} containing the corresponding HTTP
   * status and error message details.
   *
   * @param source the {@link EntityConflictException} instance to convert
   * @return a {@link MessageErrorReport} with the status set to {@link HttpStatus#CONFLICT} and a formatted error
   * message containing the entity type and query
   */
  @Override
  public MessageErrorReport convert(final @NotNull EntityConflictException source) {
    return new MessageErrorReport(
      HttpStatus.CONFLICT,
      MESSAGE.formatted(source.getRuntimeTypeName(), Objects.requireNonNullElse(source.getQuery(), "null"))
    );
  }
}
