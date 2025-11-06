package io.github.lengors.scoutdesk.domain.persistence.converters;


import io.github.lengors.scoutdesk.domain.errors.MessageErrorReport;
import io.github.lengors.scoutdesk.domain.errors.ErrorReport;
import io.github.lengors.scoutdesk.domain.persistence.exceptions.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Converts an {@link EntityNotFoundException} into an {@link ErrorReport} representation.
 * <p>
 * This class is a Spring {@link Component} and implements the {@link Converter} interface, allowing it to transform
 * {@link EntityNotFoundException} instances into structured error reports for better communication or logging of
 * errors. Typically used in scenarios where an entity cannot be found in the system based on the provided query.
 * <p>
 * The conversion logic creates a {@link MessageErrorReport} that includes: - An HTTP status code of
 * {@link HttpStatus#NOT_FOUND}. - An error message formatted using the template: "Entity {type=%s} not found for
 * {query=%s}".
 * <p>
 * The message includes the type of the entity and the query that was used in the search, which are extracted from the
 * source {@link EntityNotFoundException}. If the query is null, it is represented as the string "null".
 * <p>
 * This class facilitates structured error reporting, which is particularly useful in REST APIs to map technical
 * exceptions to standardized error responses.
 *
 * @author lengors
 */
@Component
public class EntityNotFoundExceptionReportConverter implements Converter<EntityNotFoundException, ErrorReport> {
  /**
   * A message template used to describe a scenario where an entity cannot be found in the system based on the given
   * query.
   * <p>
   * The message format includes placeholders for the type of the entity and the query used to locate it: "Entity
   * {type=%s} not found for {query=%s}".
   * <p>
   * This template is primarily utilized in the context of converting {@link EntityNotFoundException} into a structured
   * {@link MessageErrorReport}. It enables detailed, user-friendly error messages for API responses or logging, helping
   * to identify the missing entity and the query that was used during the search.
   */
  public static final String MESSAGE = "Entity {type=%s} not found for {query=%s}";

  EntityNotFoundExceptionReportConverter() {

  }

  /**
   * Converts an {@link EntityNotFoundException} to a {@link MessageErrorReport} containing the corresponding HTTP
   * status and error message details.
   *
   * @param source the {@link EntityNotFoundException} instance to convert
   * @return a {@link MessageErrorReport} with the status set to {@link HttpStatus#NOT_FOUND} and a formatted error
   * message containing the entity type and query
   */
  @Override
  public MessageErrorReport convert(final @NotNull EntityNotFoundException source) {
    return new MessageErrorReport(
      HttpStatus.NOT_FOUND,
      MESSAGE.formatted(source.getRuntimeTypeName(), Objects.requireNonNullElse(source.getQuery(), "null"))
    );
  }
}
