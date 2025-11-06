package io.github.lengors.scoutdesk.domain.persistence.configurations;

import io.github.lengors.scoutdesk.domain.persistence.models.EntityReferrer;
import io.github.lengors.scoutdesk.domain.persistence.services.EntityRetriever;
import io.github.lengors.scoutdesk.domain.resolvers.DelegatingResolutionService;
import io.github.lengors.scoutdesk.domain.resolvers.Resolver;
import io.github.lengors.scoutdesk.domain.spring.core.resolvers.TypeResolver;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
class EntityResolutionServiceConfiguration {
  @Bean
  TypeResolver<@NotNull EntityRetriever<?, ?>> entityRetrieverTypeResolver(
    final List<EntityRetriever<?, ?>> entityRetrievers
  ) {
    return new TypeResolver<>(entityRetrievers);
  }

  @Bean
  @SuppressWarnings("LineLength")
  DelegatingResolutionService<@NotNull EntityReferrer<?>, @NotNull EntityRetriever<?, ?>> entityRetrieverResolutionService(
    final List<Resolver<? super @NotNull EntityReferrer<?>, ? extends @NotNull EntityRetriever<?, ?>>> entityRetrieverResolvers
  ) {
    return new DelegatingResolutionService<>(entityRetrieverResolvers);
  }
}
