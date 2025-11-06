package io.github.lengors.scoutdesk.domain.spring.core.converters;

import io.github.lengors.scoutdesk.domain.collections.IterableConverters;
import io.github.lengors.scoutdesk.domain.errors.ConstraintError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
class ConstraintViolationErrorConverter implements Converter<ConstraintViolation<?>, ConstraintError> {
  private static final Set<ElementKind> IGNORED_ELEMENT_KINDS =
    Set.of(ElementKind.CONTAINER_ELEMENT, ElementKind.METHOD, ElementKind.PARAMETER);

  @Override
  public ConstraintError convert(final @NotNull ConstraintViolation<?> source) {
    final var propertyPath = Stream
      .concat(
        Stream.empty(),
        IterableConverters
          .stream(source.getPropertyPath())
          .filter(node -> !IGNORED_ELEMENT_KINDS.contains(node.getKind()))
          .map(Path.Node::toString))
      .collect(Collectors.joining("."));
    return new ConstraintError(propertyPath, source.getMessage());
  }
}
