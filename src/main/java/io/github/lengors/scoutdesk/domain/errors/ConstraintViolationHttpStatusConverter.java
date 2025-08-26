package io.github.lengors.scoutdesk.domain.errors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
final class ConstraintViolationHttpStatusConverter
  implements Converter<ConstraintViolation<?>, HttpStatus>, ConditionalConverter {
  private final ConversionService conversionService;

  ConstraintViolationHttpStatusConverter(@Lazy final ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @Override
  public @Nullable HttpStatus convert(final @NotNull ConstraintViolation<?> source) {
    return (@Nullable HttpStatus) conversionService.convert(
      source,
      new TypeDescriptor(
        ResolvableType.forClassWithGenerics(
          ConstraintViolation.class,
          ResolvableType.forInstance(source.getRootBean())),
        null,
        null),
      TypeDescriptor.valueOf(HttpStatus.class));
  }

  @Override
  public boolean matches(final @NotNull TypeDescriptor sourceType, final @NotNull TypeDescriptor targetType) {
    return !sourceType
      .getResolvableType()
      .hasResolvableGenerics();
  }
}
