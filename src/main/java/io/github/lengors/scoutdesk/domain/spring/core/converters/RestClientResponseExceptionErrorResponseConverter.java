package io.github.lengors.scoutdesk.domain.spring.core.converters;

import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.client.RestClientResponseException;

@Component
final class RestClientResponseExceptionErrorResponseConverter
  implements Converter<RestClientResponseException, ErrorResponse> {
  @Override
  public ErrorResponse convert(final @NotNull RestClientResponseException source) {
    return ErrorResponse.create(source, source.getStatusCode(), source.getResponseBodyAsString());
  }
}
