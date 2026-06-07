package io.github.lengors.scoutdesk.domain.spring.core.converters;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

@Component
class ConstraintViolationHttpStatusCodeConverter
  implements ConditionalConverter, Converter<ConstraintViolation<?>, HttpStatusCode> {
  private final ConversionService conversionService;

  ConstraintViolationHttpStatusCodeConverter(@Lazy final ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @Override
  public @Nullable HttpStatusCode convert(final @NotNull ConstraintViolation<?> source) {
    return (@Nullable HttpStatusCode) conversionService.convert(
      source,
      new TypeDescriptor(
        ResolvableType.forClassWithGenerics(
          ConstraintViolation.class,
          ResolvableType.forInstance(source.getRootBean())
        ),
        null,
        null
      ),
      TypeDescriptor.valueOf(HttpStatusCode.class)
    );
  }

  @Override
  public boolean matches(final @NotNull TypeDescriptor sourceType, final @NotNull TypeDescriptor targetType) {
    return !sourceType
      .getResolvableType()
      .hasResolvableGenerics();
  }
}
