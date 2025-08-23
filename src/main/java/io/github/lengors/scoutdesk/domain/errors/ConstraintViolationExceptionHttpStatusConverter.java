package io.github.lengors.scoutdesk.domain.errors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
final class ConstraintViolationExceptionHttpStatusConverter
  implements Converter<ConstraintViolationException, HttpStatus> {
  private final ConversionService conversionService;

  ConstraintViolationExceptionHttpStatusConverter(@Lazy final ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @Override
  public HttpStatus convert(final @NotNull ConstraintViolationException source) {
    return source
      .getConstraintViolations()
      .stream()
      .map(constraintViolation -> Optional.ofNullable(conversionService.convert(constraintViolation, HttpStatus.class)))
      .filter(Optional::isPresent)
      .map(Optional::orElseThrow)
      .distinct()
      .reduce((left, right) -> !Objects.equals(left, right) ? HttpStatus.BAD_REQUEST : left)
      .orElse(HttpStatus.BAD_REQUEST);
  }

  private @Nullable HttpStatus convert(final ConstraintViolation<?> constraintViolation) {
    return (@Nullable HttpStatus) conversionService.convert(
      constraintViolation,
      new TypeDescriptor(ResolvableType.forClassWithGenerics(ConstraintViolation.class,
        ResolvableType.forInstance(constraintViolation.getRootBean())), null, null),
      TypeDescriptor.valueOf(HttpStatus.class));
  }
}
