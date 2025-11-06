package io.github.lengors.scoutdesk.domain.spring.core.converters;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatusCode;

/**
 * Base class for conditional converters from {@link ConstraintViolation} to {@link HttpStatusCode}. The converter
 * matches if the root bean type of the constraint violation is assignable from the requested type.
 *
 * @param <T> the type of the root bean of the constraint violation
 * @author lengors
 */
public abstract class AbstractConstraintViolationHttpStatusCodeConditionalConverter<T>
  implements ConditionalConverter, Converter<ConstraintViolation<T>, HttpStatusCode> {
  private final ResolvableType requestedSourceRootBeanType;

  /**
   * Constructs a new converter for the given root bean class.
   *
   * @param requestedSourceRootBeanClass the class of the root bean of the constraint violation
   */
  protected AbstractConstraintViolationHttpStatusCodeConditionalConverter(
    final @NotNull Class<T> requestedSourceRootBeanClass
  ) {
    this.requestedSourceRootBeanType = ResolvableType.forClass(requestedSourceRootBeanClass);
  }

  @Override
  public final boolean matches(final @NotNull TypeDescriptor sourceType, final @NotNull TypeDescriptor targetType) {
    final var resolvableType = sourceType.getResolvableType();
    if (!resolvableType.hasResolvableGenerics()) {
      return false;
    }
    final var sourceHandlerType = resolvableType.getGeneric(0);
    return requestedSourceRootBeanType.isAssignableFrom(sourceHandlerType);
  }
}
