package io.github.lengors.scoutdesk.domain.spring.security.services;

import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration(proxyBeanMethods = false)
class UserHandlerMethodArgumentResolverConfiguration implements WebMvcConfigurer {
  private final UserHandlerMethodArgumentResolver userHandlerMethodArgumentResolver;

  UserHandlerMethodArgumentResolverConfiguration(
    final UserHandlerMethodArgumentResolver userHandlerMethodArgumentResolver
  ) {
    this.userHandlerMethodArgumentResolver = userHandlerMethodArgumentResolver;
  }

  @Override
  public void addArgumentResolvers(final @NotNull List<HandlerMethodArgumentResolver> resolvers) {
    WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    resolvers.add(userHandlerMethodArgumentResolver);
  }
}
