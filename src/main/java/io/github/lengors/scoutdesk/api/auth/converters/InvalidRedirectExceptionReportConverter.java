package io.github.lengors.scoutdesk.api.auth.converters;

import io.github.lengors.scoutdesk.api.auth.exceptions.InvalidRedirectException;
import io.github.lengors.scoutdesk.domain.errors.ErrorReport;
import io.github.lengors.scoutdesk.domain.errors.MessageErrorReport;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
class InvalidRedirectExceptionReportConverter implements Converter<InvalidRedirectException, ErrorReport> {
  @Override
  public @Nullable ErrorReport convert(final @NotNull InvalidRedirectException source) {
    return new MessageErrorReport(
      HttpStatus.BAD_REQUEST,
      "Unauthorized redirect to: %s".formatted(source.getRedirect())
    );
  }
}
