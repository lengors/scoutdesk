package io.github.lengors.scoutdesk.domain.spring.core.services;

import io.github.lengors.scoutdesk.domain.metadata.MetadataRegistry;
import io.github.lengors.scoutdesk.domain.spring.core.models.AnnotatedRequestQualifier;
import io.github.lengors.scoutdesk.domain.spring.core.models.RequestQualifier;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.lang.reflect.Type;

@ControllerAdvice
class RequestBodyPartAdvice implements RequestBodyAdvice {
  private final MetadataRegistry<@NotNull RequestQualifier> requestQualifiers;

  RequestBodyPartAdvice(final MetadataRegistry<@NotNull RequestQualifier> requestQualifiers) {
    this.requestQualifiers = requestQualifiers;
  }

  @Override
  @NotNull
  public Object afterBodyRead(
    final @NotNull Object body,
    final @NotNull HttpInputMessage inputMessage,
    final @NotNull MethodParameter parameter,
    final @NotNull Type targetType,
    final @NotNull Class<? extends HttpMessageConverter<?>> converterType
  ) {
    rebind(inputMessage, body, parameter);
    return body;
  }

  @Override
  @NotNull
  public HttpInputMessage beforeBodyRead(
    final @NotNull HttpInputMessage inputMessage,
    final @NotNull MethodParameter parameter,
    final @NotNull Type targetType,
    final @NotNull Class<? extends HttpMessageConverter<?>> converterType
  ) {
    rebind(null, inputMessage, parameter);
    return inputMessage;
  }

  @Override
  public @Nullable Object handleEmptyBody(
    final @Nullable Object body,
    final @NotNull HttpInputMessage inputMessage,
    final @NotNull MethodParameter parameter,
    final @NotNull Type targetType,
    final @NotNull Class<? extends HttpMessageConverter<?>> converterType
  ) {
    rebind(inputMessage, body, parameter);
    return body;
  }

  private void rebind(
    final @Nullable Object oldSource,
    final @Nullable Object newSource,
    final @NotNull MethodParameter parameter
  ) {
    if (newSource == null) {
      return;
    }
    if (oldSource != null) {
      requestQualifiers.unbind(oldSource);
    }
    final var requestPart = parameter.getParameterAnnotation(RequestPart.class);
    if (requestPart != null) {
      requestQualifiers.bind(newSource, new AnnotatedRequestQualifier(requestPart));
    }
  }

  @Override
  public boolean supports(
    final @NotNull MethodParameter methodParameter,
    final @NotNull Type targetType,
    final @NotNull Class<? extends HttpMessageConverter<?>> converterType
  ) {
    return methodParameter.hasParameterAnnotation(RequestPart.class);
  }
}
