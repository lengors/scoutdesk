package io.github.lengors.scoutdesk.domain.spring.core.converters;

import io.github.lengors.scoutdesk.domain.errors.ErrorReport;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class ConversionFailedExceptionReportConverter implements Converter<ConversionFailedException, ErrorReport> {
  private final ConversionService conversionService;

  ConversionFailedExceptionReportConverter(@Lazy final ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @Override
  public @Nullable ErrorReport convert(final @NotNull ConversionFailedException source) {
    final var cause = source.getCause();
    return cause != null ? conversionService.convert(cause, ErrorReport.class) : null;
  }
}
