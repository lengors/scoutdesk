package io.github.lengors.scoutdesk.domain.spring.io.converters;

import io.github.lengors.scoutdesk.domain.io.models.DataSourceDescriptor;
import io.github.lengors.scoutdesk.domain.metadata.MetadataRegistry;
import io.github.lengors.scoutdesk.domain.spring.core.models.RequestQualifier;
import io.github.lengors.scoutdesk.domain.spring.core.models.SimpleRequestQualifier;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Component
class MultipartFileObjectConverter implements ConditionalGenericConverter {
  private static final TypeDescriptor MULTIPART_FILE_DATA_SOURCE_TYPE_DESCRIPTOR = new TypeDescriptor(
    ResolvableType.forClassWithGenerics(DataSourceDescriptor.class, MultipartFile.class),
    null,
    null
  );

  private final MetadataRegistry<@NotNull RequestQualifier> requestQualifiers;
  private final ConversionService conversionService;

  MultipartFileObjectConverter(
    final MetadataRegistry<@NotNull RequestQualifier> requestQualifiers,
    @Lazy final ConversionService conversionService
  ) {
    this.conversionService = conversionService;
    this.requestQualifiers = requestQualifiers;
  }

  @Override
  @Nullable
  public Object convert(
    final @Nullable Object source,
    final @NotNull TypeDescriptor sourceType,
    final @NotNull TypeDescriptor targetType
  ) {
    if (!(source instanceof MultipartFile multipartFile)) {
      return null;
    }

    final var dataSourceDescriptor =
      new DataSourceDescriptor<>(multipartFile.getContentType(), multipartFile, MultipartFile::getInputStream);
    final var output = conversionService.convert(dataSourceDescriptor, targetType);
    if (output == null) {
      return null;
    }

    requestQualifiers.bind(output, new SimpleRequestQualifier(multipartFile.getName()));
    return output;
  }

  @Override
  @Nullable
  public Set<ConvertiblePair> getConvertibleTypes() {
    return null;
  }

  @Override
  public boolean matches(
    final @NotNull TypeDescriptor sourceType,
    final @NotNull TypeDescriptor targetType
  ) {
    final var isMultipartFileSourceType = MultipartFile.class.isAssignableFrom(sourceType.getType());
    return isMultipartFileSourceType
      && conversionService.canConvert(MULTIPART_FILE_DATA_SOURCE_TYPE_DESCRIPTOR, targetType);
  }
}
