package io.github.lengors.scoutdesk.domain.errors;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.util.NullnessUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
final class ConstraintViolationExceptionProblemDetailConverter
  implements Converter<ConstraintViolationException, ProblemDetail> {
  private final ConversionService conversionService;

  ConstraintViolationExceptionProblemDetailConverter(@Lazy final ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @Override
  public ProblemDetail convert(final @NotNull ConstraintViolationException source) {
    final var errors = source
      .getConstraintViolations()
      .stream()
      .map(constraintViolation -> Optional.ofNullable(
        conversionService.convert(constraintViolation, NullnessUtil.castNonNull(PropertyError.class))))
      .filter(Optional::isPresent)
      .map(Optional::orElseThrow)
      .toList();

    final var httpStatus = conversionService.convert(source, HttpStatus.class);
    final var problemDetail = ProblemDetail.forStatus(httpStatus == null ? HttpStatus.BAD_REQUEST : httpStatus);
    problemDetail.setProperty("errors", errors);

    return problemDetail;
  }
}
