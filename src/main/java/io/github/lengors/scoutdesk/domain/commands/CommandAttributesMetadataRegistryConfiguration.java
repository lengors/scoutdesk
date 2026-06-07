package io.github.lengors.scoutdesk.domain.commands;

import io.github.lengors.scoutdesk.domain.metadata.EphemeralMetadataRegistry;
import io.github.lengors.scoutdesk.domain.metadata.MetadataRegistry;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration(proxyBeanMethods = false)
class CommandAttributesMetadataRegistryConfiguration {
  @Bean(name = "commandAttributes")
  MetadataRegistry<@NotNull Map<Object, Object>> commandAttributesMetadataRegistry() {
    return new EphemeralMetadataRegistry<>();
  }
}
