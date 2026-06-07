package io.github.lengors.scoutdesk.domain.io.converters;

import io.github.lengors.scoutdesk.domain.errors.ConstraintError;
import io.github.lengors.scoutdesk.domain.errors.ConstraintErrorReport;
import io.github.lengors.scoutdesk.domain.errors.ErrorReport;
import io.github.lengors.scoutdesk.domain.io.exceptions.LoadDataException;
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
class LoadDataExceptionReportConverter implements Converter<LoadDataException, ErrorReport> {
  private final ConversionService conversionService;

  LoadDataExceptionReportConverter(@Lazy final ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @Override
  public ErrorReport convert(final @NotNull LoadDataException source) {
    final var httpStatusCode = conversionService.convert(source, HttpStatusCode.class);
    final var constraintError = conversionService.convert(source, NullnessUtil.castNonNull(ConstraintError.class));
    return new ConstraintErrorReport(
      Objects.requireNonNullElse(httpStatusCode, HttpStatus.BAD_REQUEST),
      Optional
        .ofNullable(constraintError)
        .stream()
        .toList());
  }
}
