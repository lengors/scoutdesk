package io.github.lengors.scoutdesk.domain.errors;

import io.github.lengors.scoutdesk.domain.collections.IterableConverters;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
final class ConstraintViolationPropertyErrorConverter implements Converter<ConstraintViolation<?>, PropertyError> {
  @Override
  public PropertyError convert(final @NotNull ConstraintViolation<?> source) {
    final var propertyPath = IterableConverters
      .stream(source.getPropertyPath())
      .skip(2)
      .filter(node -> node.getKind() != ElementKind.CONTAINER_ELEMENT)
      .map(Path.Node::toString)
      .collect(Collectors.joining("."));
    return new PropertyError(propertyPath, source.getMessage());
  }
}
