package io.github.lengors.scoutdesk.domain.spring.core.converters;

import io.github.lengors.scoutdesk.domain.collections.IterableConverters;
import io.github.lengors.scoutdesk.domain.errors.ConstraintError;
import io.github.lengors.scoutdesk.domain.metadata.MetadataRegistry;
import io.github.lengors.scoutdesk.domain.spring.core.models.RequestQualifier;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.util.NullnessUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
class ConstraintViolationErrorConverter implements Converter<ConstraintViolation<?>, ConstraintError> {
  private static final Set<ElementKind> IGNORED_ELEMENT_KINDS =
    Set.of(ElementKind.CONTAINER_ELEMENT, ElementKind.METHOD, ElementKind.PARAMETER);

  private final MetadataRegistry<@NotNull RequestQualifier> requestQualifiers;

  ConstraintViolationErrorConverter(final MetadataRegistry<@NotNull RequestQualifier> requestQualifiers) {
    this.requestQualifiers = requestQualifiers;
  }

  @Override
  public ConstraintError convert(final @NotNull ConstraintViolation<?> source) {
    final var propertyPath = Stream
      .concat(
        requestQualifiers
          .stream(
            Optional
              .ofNullable(source.getInvalidValue())
              .or(() -> IterableConverters
                .stream(source.getPropertyPath())
                .filter(node -> ElementKind.PARAMETER.equals(node.getKind()) && node instanceof Path.ParameterNode)
                .map(Path.ParameterNode.class::cast)
                .findFirst()
                .map(node -> {
                  final var executableParameters = source.getExecutableParameters();
                  final var parameterIndex = NullnessUtil
                    .castNonNull(node)
                    .getParameterIndex();
                  return parameterIndex < executableParameters.length ? executableParameters[parameterIndex] : null;
                }))
              .orElseGet(source::getLeafBean))
          .map(RequestQualifier::name),
        IterableConverters
          .stream(source.getPropertyPath())
          .filter(node -> !IGNORED_ELEMENT_KINDS.contains(node.getKind()))
          .map(Path.Node::toString))
      .collect(Collectors.joining("."));
    return new ConstraintError(
      propertyPath,
      source.getMessage(),
      NullnessUtil.castNonNull(source
        .getConstraintDescriptor()
        .getAnnotation()
        .annotationType()
        .getCanonicalName()));
  }
}
