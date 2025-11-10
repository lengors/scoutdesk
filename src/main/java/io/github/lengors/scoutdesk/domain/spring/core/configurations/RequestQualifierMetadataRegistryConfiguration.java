package io.github.lengors.scoutdesk.domain.spring.core.configurations;

import io.github.lengors.scoutdesk.domain.metadata.EphemeralMetadataRegistry;
import io.github.lengors.scoutdesk.domain.metadata.MetadataRegistry;
import io.github.lengors.scoutdesk.domain.spring.core.models.RequestQualifier;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
class RequestQualifierMetadataRegistryConfiguration {
  @Bean(name = "requestQualifiers")
  MetadataRegistry<@NotNull RequestQualifier> requestQualifiersMetadataRegistry() {
    return new EphemeralMetadataRegistry<>();
  }
}
