package io.github.lengors.scoutdesk.domain.scrapers.specifications.converters;

import io.github.lengors.scoutdesk.domain.scrapers.specifications.exceptions.ScraperOwnedSpecificationStatusTransitionException;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;

@Component
final class ScraperOwnedSpecificationStatusTransitionExceptionErrorResponseConverter
  implements Converter<ScraperOwnedSpecificationStatusTransitionException, ErrorResponse> {
  @Override
  public ErrorResponse convert(final @NotNull ScraperOwnedSpecificationStatusTransitionException source) {
    return ErrorResponse.create(
      source,
      HttpStatus.UNPROCESSABLE_ENTITY,
      "Invalid status transition from %s to %s".formatted(source.getFrom(), source.getTo())
    );
  }
}
