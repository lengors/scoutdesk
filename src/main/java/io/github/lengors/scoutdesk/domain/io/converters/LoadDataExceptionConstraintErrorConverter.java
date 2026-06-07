package io.github.lengors.scoutdesk.domain.io.converters;

import io.github.lengors.scoutdesk.domain.errors.ConstraintError;
import io.github.lengors.scoutdesk.domain.io.exceptions.LoadDataException;
import io.github.lengors.scoutdesk.domain.metadata.MetadataRegistry;
import io.github.lengors.scoutdesk.domain.spring.core.models.RequestQualifier;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.util.NullnessUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
class LoadDataExceptionConstraintErrorConverter implements Converter<LoadDataException, ConstraintError> {
  private final MetadataRegistry<@NotNull RequestQualifier> requestQualifiers;

  LoadDataExceptionConstraintErrorConverter(final MetadataRegistry<@NotNull RequestQualifier> requestQualifiers) {
    this.requestQualifiers = requestQualifiers;
  }

  @Override
  public @Nullable ConstraintError convert(final @NotNull LoadDataException source) {
    final var property = requestQualifiers
      .stream(source.getSource())
      .map(RequestQualifier::name)
      .collect(Collectors.joining("."));
    if (StringUtils.isBlank(property)) {
      return null;
    }

    final var message = source.getMessage();
    if (message == null || StringUtils.isBlank(message)) {
      return null;
    }

    return new ConstraintError(
      property,
      message,
      NullnessUtil.castNonNull(source
        .getClass()
        .getCanonicalName()));
  }
}
