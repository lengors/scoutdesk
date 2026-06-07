package io.github.lengors.scoutdesk.domain.io.converters;

import io.github.lengors.scoutdesk.domain.io.exceptions.LoadDataException;
import io.github.lengors.scoutdesk.domain.io.exceptions.LoadDataFormatException;
import io.github.lengors.scoutdesk.domain.io.exceptions.LoadDataIOException;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

@Component
class LoadDataExceptionHttpStatusCodeConverter implements Converter<LoadDataException, HttpStatusCode> {
  @Override
  public HttpStatusCode convert(final @NotNull LoadDataException source) {
    return switch (source) {
      case LoadDataFormatException ignored -> HttpStatus.UNPROCESSABLE_ENTITY;
      case LoadDataIOException ignored -> HttpStatus.INTERNAL_SERVER_ERROR;
    };
  }
}
