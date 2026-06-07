package io.github.lengors.scoutdesk.domain.spring.core.converters;

import io.github.lengors.scoutdesk.domain.errors.ErrorReport;
import io.github.lengors.scoutdesk.domain.errors.MessageErrorReport;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

@Component
class RestClientResponseExceptionReportConverter implements Converter<RestClientResponseException, ErrorReport> {
  @Override
  public MessageErrorReport convert(final @NotNull RestClientResponseException source) {
    return new MessageErrorReport(source.getStatusCode(), source.getResponseBodyAsString());
  }
}
