package io.github.lengors.scoutdesk.domain.spring.security.services;

import io.github.lengors.scoutdesk.domain.spring.security.models.User;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.util.NullnessUtil;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@Component
class UserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
  private final ConversionService conversionService;

  UserHandlerMethodArgumentResolver(@Lazy final ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @Override
  public boolean supportsParameter(final @NotNull MethodParameter parameter) {
    return NullnessUtil
      .castNonNull(User.class)
      .isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public @Nullable User resolveArgument(
    final @NotNull MethodParameter parameter,
    final @Nullable ModelAndViewContainer mavContainer,
    final @NotNull NativeWebRequest webRequest,
    final @Nullable WebDataBinderFactory binderFactory
  ) {
    return Optional
      .ofNullable(webRequest.getUserPrincipal())
      .map(it -> conversionService.convert(it, NullnessUtil.castNonNull(User.class)))
      .orElse(null);
  }
}
