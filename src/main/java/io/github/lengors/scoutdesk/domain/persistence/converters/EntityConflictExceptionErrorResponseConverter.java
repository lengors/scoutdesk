package io.github.lengors.scoutdesk.domain.persistence.converters;

import io.github.lengors.scoutdesk.domain.persistence.exceptions.EntityConflictException;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;

import java.util.Objects;

@Component
final class EntityConflictExceptionErrorResponseConverter implements Converter<EntityConflictException, ErrorResponse> {
  @Override
  public ErrorResponse convert(final @NotNull EntityConflictException source) {
    return ErrorResponse.create(
      source,
      HttpStatus.CONFLICT,
      "Entity {type=%s} cannot be deleted because at least one of {query=%s} depends on it".formatted(
        source.getRuntimeTypeName(),
        Objects.requireNonNullElse(source.getQuery(), "null")
      ));
  }
}
