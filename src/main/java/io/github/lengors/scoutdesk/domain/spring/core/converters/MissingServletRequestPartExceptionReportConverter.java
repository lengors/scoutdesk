package io.github.lengors.scoutdesk.domain.spring.core.converters;

import io.github.lengors.scoutdesk.domain.errors.ConstraintError;
import io.github.lengors.scoutdesk.domain.errors.ConstraintErrorReport;
import io.github.lengors.scoutdesk.domain.errors.ErrorReport;
import io.github.lengors.scoutdesk.domain.errors.MessageErrorReport;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.util.NullnessUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Objects;
import java.util.Optional;

@Component
class MissingServletRequestPartExceptionReportConverter
  implements Converter<MissingServletRequestPartException, ErrorReport> {
  private final ConversionService conversionService;

  MissingServletRequestPartExceptionReportConverter(@Lazy final ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @Override
  public @Nullable ErrorReport convert(final @NotNull MissingServletRequestPartException source) {
    final var errors = Optional
      .ofNullable(conversionService.convert(source, NullnessUtil.castNonNull(ConstraintError.class)))
      .stream()
      .toList();
    final var httpStatusCode = conversionService.canConvert(source.getClass(), HttpStatusCode.class)
      ? Objects.requireNonNullElseGet(conversionService.convert(source, HttpStatusCode.class), source::getStatusCode)
      : source.getStatusCode();
    if (errors.isEmpty()) {
      return Optional
        .ofNullable(source.getMessage())
        .<ErrorReport>map(it -> new MessageErrorReport(httpStatusCode, it))
        .orElse(null);
    }

    return new ConstraintErrorReport(httpStatusCode, errors);
  }
}
