package io.github.lengors.scoutdesk.domain.scrapers.specifications.converters;

import io.github.lengors.scoutdesk.domain.errors.ErrorReport;
import io.github.lengors.scoutdesk.domain.errors.MessageErrorReport;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.exceptions.ScraperOwnedSpecificationStatusTransitionException;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Converts a {@link ScraperOwnedSpecificationStatusTransitionException} into an {@link ErrorReport}.
 * <p>
 * This converter handles the transformation of exceptions caused by invalid status transitions in a scraper-owned
 * specification into a structured error report format. The error report contains an HTTP status code and a message that
 * describes the invalid transition.
 * <p>
 * The conversion process uses the {@link HttpStatus#UNPROCESSABLE_ENTITY} status code as a representation of the error.
 * The message details include the starting and target states of the attempted invalid transition.
 * <p>
 * This class is a Spring {@link Component}, which makes it discoverable and automatically instantiated in Spring's
 * application context.
 *
 * @author lengors
 */
@Component
public class ScraperOwnedSpecificationStatusTransitionExceptionReportConverter
  implements Converter<ScraperOwnedSpecificationStatusTransitionException, ErrorReport> {

  /**
   * A string template used to generate error messages for invalid status transitions.
   * <p>
   * The message includes placeholders for specifying the current status and the target status of a failed transition
   * attempt. This is particularly used in error reporting scenarios to provide more context to the user or system
   * consuming the error report.
   * <p>
   * The template expects two arguments:
   * <ul>
   * <li>The starting status (`from`)</li>
   * <li>The requested target status (`to`)</li>
   * </ul>
   * For example, it can be formatted to produce messages like:
   * "Invalid status transition from [CURRENT_STATUS] to [TARGET_STATUS]".
   */
  public static final String MESSAGE = "Invalid status transition from %s to %s";

  ScraperOwnedSpecificationStatusTransitionExceptionReportConverter() {

  }

  /**
   * Converts a {@link ScraperOwnedSpecificationStatusTransitionException} into a {@link MessageErrorReport}.
   * <p>
   * This method handles transformations of exceptions caused by invalid status transitions into structured error report
   * formats consisting of an HTTP status code and an informative message.
   *
   * @param source the {@link ScraperOwnedSpecificationStatusTransitionException} containing details of the invalid
   *               status transition
   * @return a {@link MessageErrorReport} containing the HTTP status code {@link HttpStatus#UNPROCESSABLE_ENTITY} and a
   * message describing the invalid transition
   */
  @Override
  public MessageErrorReport convert(final @NotNull ScraperOwnedSpecificationStatusTransitionException source) {
    return new MessageErrorReport(HttpStatus.UNPROCESSABLE_ENTITY, MESSAGE.formatted(source.getFrom(), source.getTo()));
  }
}
