package io.github.lengors.scoutdesk.domain.spring.core.converters;

import io.github.lengors.scoutdesk.domain.errors.ConstraintError;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * Converts a {@link MissingServletRequestPartException} into a {@link ConstraintError}. This converter is designed to
 * handle cases where a required multipart request part is missing, and provides a constraint error with a message
 * indicating the missing field name.
 * <p>
 * The conversion logic extracts the name of the missing request part from the exception and creates a
 * {@link ConstraintError} instance with a formatted message.
 * <p>
 * This component is registered as a Spring {@link Component} and implements the {@link Converter} interface for the
 * types {@link MissingServletRequestPartException} and {@link ConstraintError}.
 *
 * @author lengors
 */
@Component
public class MissingServletRequestPartExceptionConstraintErrorConverter
  implements Converter<MissingServletRequestPartException, ConstraintError> {
  /**
   * A constant string template used to indicate a missing field in a multipart request. The placeholder "%s" within the
   * string is intended to be replaced with the specific name of the missing field.
   */
  public static final String MESSAGE = "missing %s field";

  /**
   * Default constructor for the MissingServletRequestPartExceptionConstraintErrorConverter class. This is typically
   * utilized internally by Spring when the component is initialized. The converter facilitates the transformation of a
   * MissingServletRequestPartException into a ConstraintError by providing meaningful error messages for missing
   * multipart request parts.
   */
  MissingServletRequestPartExceptionConstraintErrorConverter() {
    // Empty constructor for protecting against unwanted instantiation
  }

  /**
   * Converts a {@link MissingServletRequestPartException} into a {@link ConstraintError}. This method is used to handle
   * cases where a required multipart request part is missing. It creates a {@link ConstraintError} object with a
   * property indicating the missing field name and a corresponding error message.
   *
   * @param source the {@link MissingServletRequestPartException} instance representing the missing multipart request
   *               part
   * @return a {@link ConstraintError} instance containing the missing field name and an error message
   */
  @Override
  public ConstraintError convert(final @NotNull MissingServletRequestPartException source) {
    return new ConstraintError(source.getRequestPartName(), MESSAGE.formatted(source.getRequestPartName()));
  }
}
