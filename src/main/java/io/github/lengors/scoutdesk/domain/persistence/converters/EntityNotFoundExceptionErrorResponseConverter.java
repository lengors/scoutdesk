package io.github.lengors.scoutdesk.domain.persistence.converters;

import io.github.lengors.scoutdesk.domain.persistence.exceptions.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;

import java.util.Objects;

@Component
final class EntityNotFoundExceptionErrorResponseConverter implements Converter<EntityNotFoundException, ErrorResponse> {
  @Override
  public ErrorResponse convert(final @NotNull EntityNotFoundException source) {
    return ErrorResponse.create(
      source,
      HttpStatus.NOT_FOUND,
      "Entity {type=%s} not found for {query=%s}".formatted(
        source.getRuntimeTypeName(),
        Objects.requireNonNullElse(source.getQuery(), "null")
      )
    );
  }
}
