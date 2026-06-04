package io.github.lengors.scoutdesk.domain.spring.security.configurations;

import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ReactorClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;

@Configuration(proxyBeanMethods = false)
class ClientCustomizerConfiguration {
  @Bean
  RestClientCustomizer restClientBuilderCustomizer() {
    return builder -> builder.requestFactory(new ReactorClientHttpRequestFactory(HttpClient.newConnection()));
  }

  @Bean
  WebClientCustomizer webClientCustomizer() {
    return builder -> builder.clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection()));
  }
}
