package io.github.lengors.scoutdesk.domain.io.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration(proxyBeanMethods = false)
class ObjectMapperConfiguration {

  @Primary
  @Bean(name = "jsonObjectMapper")
  ObjectMapper jsonObjectMapper(final Jackson2ObjectMapperBuilder builder) {
    return builder
      .createXmlMapper(false)
      .build();
  }

  @Bean(name = "yamlObjectMapper")
  ObjectMapper yamlObjectMapper(final ObjectMapper objectMapper) {
    return objectMapper.copyWith(new YAMLFactory());
  }
}
