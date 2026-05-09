package io.github.lengors.scoutdesk.domain.commands;

import io.github.lengors.scoutdesk.domain.errors.ErrorReport;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class CommandExceptionReportConverter implements Converter<CommandException, ErrorReport> {
  private final ConversionService conversionService;

  CommandExceptionReportConverter(@Lazy final ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @Override
  public @Nullable ErrorReport convert(final @NotNull CommandException source) {
    final var underlyingCause = source.getUnderlyingCause();
    return underlyingCause != null ? conversionService.convert(underlyingCause, ErrorReport.class) : null;
  }
}
