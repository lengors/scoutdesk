package io.github.lengors.scoutdesk.domain.spring.core.converters;

import io.github.lengors.scoutdesk.domain.errors.ConstraintError;
import io.github.lengors.scoutdesk.domain.errors.ConstraintErrorReport;
import io.github.lengors.scoutdesk.domain.errors.ErrorReport;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;

import org.checkerframework.checker.nullness.util.NullnessUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
class ConstraintViolationExceptionReportConverter implements Converter<ConstraintViolationException, ErrorReport> {
  private final ConversionService conversionService;

  ConstraintViolationExceptionReportConverter(@Lazy final ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @Override
  public ConstraintErrorReport convert(final @NotNull ConstraintViolationException source) {
    final var constraintErrors = source
      .getConstraintViolations()
      .stream()
      .map(constraintViolation -> Optional.ofNullable(
        conversionService.convert(constraintViolation, NullnessUtil.castNonNull(ConstraintError.class))))
      .filter(Optional::isPresent)
      .map(Optional::orElseThrow)
      .toList();

    final var statusCode = conversionService.convert(source, HttpStatusCode.class);
    return new ConstraintErrorReport(Objects.requireNonNullElse(statusCode, HttpStatus.BAD_REQUEST), constraintErrors);
  }
}
