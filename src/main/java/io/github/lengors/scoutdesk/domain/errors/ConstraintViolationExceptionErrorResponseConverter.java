package io.github.lengors.scoutdesk.domain.errors;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;

@Component
final class ConstraintViolationExceptionErrorResponseConverter
  implements Converter<ConstraintViolationException, ErrorResponse> {
  private final ConversionService conversionService;

  ConstraintViolationExceptionErrorResponseConverter(@Lazy final ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @Override
  public ErrorResponse convert(final @NotNull ConstraintViolationException source) {
    final var problemDetail = conversionService.convert(source, ProblemDetail.class);
    return ErrorResponse
      .builder(source, problemDetail == null ? ProblemDetail.forStatus(HttpStatus.BAD_REQUEST) : problemDetail)
      .build();
  }
}
