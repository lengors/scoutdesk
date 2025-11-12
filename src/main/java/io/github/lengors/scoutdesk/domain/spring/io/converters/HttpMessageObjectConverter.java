package io.github.lengors.scoutdesk.domain.spring.io.converters;

import io.github.lengors.scoutdesk.domain.io.models.DataSourceDescriptor;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
class HttpMessageObjectConverter implements HttpMessageConverter<Object> {
  private static final TypeDescriptor HTTP_INPUT_MESSAGE_DATA_SOURCE_TYPE_DESCRIPTOR = new TypeDescriptor(
    ResolvableType.forClassWithGenerics(DataSourceDescriptor.class, HttpInputMessage.class),
    null,
    null
  );

  private static final Set<MediaType> SUPPORTED_MEDIA_TYPES = Set.of(
    MediaType.APPLICATION_JSON,
    MediaType.APPLICATION_YAML,
    MediaType.APPLICATION_OCTET_STREAM,
    MediaType.MULTIPART_FORM_DATA);

  private final ConversionService conversionService;

  HttpMessageObjectConverter(final ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @Override
  public boolean canRead(
    final @NotNull Class<?> type,
    final @Nullable MediaType mediaType
  ) {
    final var isMediaTypeSupported = Optional
      .ofNullable(mediaType)
      .map(it -> SUPPORTED_MEDIA_TYPES
        .stream()
        .anyMatch(it::isCompatibleWith))
      .orElse(false);
    if (!isMediaTypeSupported) {
      return false;
    }

    final var typeDescriptor = TypeDescriptor.valueOf(type);
    final var hasUnresolvedGenerics = typeDescriptor
      .getResolvableType()
      .hasUnresolvableGenerics();
    return !hasUnresolvedGenerics
      && conversionService.canConvert(HTTP_INPUT_MESSAGE_DATA_SOURCE_TYPE_DESCRIPTOR, typeDescriptor);
  }

  @Override
  public boolean canWrite(
    final @NotNull Class<?> type,
    final @Nullable MediaType mediaType
  ) {
    return false;
  }

  @Override
  @NotNull
  public List<MediaType> getSupportedMediaTypes() {
    return List.copyOf(SUPPORTED_MEDIA_TYPES);
  }

  @Override
  @NotNull
  public Object read(
    final @NotNull Class<?> type,
    final @NotNull HttpInputMessage inputMessage
  ) throws HttpMessageNotReadableException {
    final var contentType = Optional
      .ofNullable(inputMessage
        .getHeaders()
        .getContentType())
      .map(MediaType::toString)
      .orElse(null);

    final var dataSourceDescriptor = new DataSourceDescriptor<>(contentType, inputMessage, HttpInputMessage::getBody);
    final var output = conversionService.convert(dataSourceDescriptor, type);
    if (output == null) {
      throw new HttpMessageNotReadableException("Could not read request body", inputMessage);
    }

    return output;
  }

  @Override
  public void write(
    final @NotNull Object readable,
    final @Nullable MediaType contentType,
    final @NotNull HttpOutputMessage outputMessage
  ) throws HttpMessageNotWritableException {
    throw new UnsupportedOperationException("Writing is not supported");
  }
}
