package io.github.lengors.scoutdesk.domain.commands;

import io.github.lengors.scoutdesk.domain.resolvers.DelegatingResolutionService;
import io.github.lengors.scoutdesk.domain.resolvers.Resolver;
import io.github.lengors.scoutdesk.domain.spring.core.resolvers.TypeResolver;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
class CommandHandlerResolutionServiceConfiguration {
  @Bean
  TypeResolver<@NotNull CommandHandler<?, ?, ?>> commandHandlerTypeResolver(
    final List<CommandHandler<?, ?, ?>> commandHandlers
  ) {
    return new TypeResolver<>(commandHandlers);
  }

  @Bean
  @SuppressWarnings("LineLength")
  DelegatingResolutionService<@NotNull Command<?, ?>, @NotNull CommandHandler<?, ?, ?>> commandHandlerResolutionService(
    final List<Resolver<? super @NotNull Command<?, ?>, ? extends @NotNull CommandHandler<?, ?, ?>>> commandHandlerResolvers
  ) {
    return new DelegatingResolutionService<>(commandHandlerResolvers);
  }
}
