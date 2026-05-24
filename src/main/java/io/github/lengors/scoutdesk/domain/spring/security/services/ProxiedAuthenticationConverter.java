package io.github.lengors.scoutdesk.domain.spring.security.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.lengors.scoutdesk.domain.spring.security.models.ProxiedAuthentication;
import io.github.lengors.scoutdesk.domain.spring.security.properties.ProxiedAuthenticationProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.EnumerationUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@ConditionalOnProperty(ProxiedAuthenticationProperties.PREFIX + ".header.prefix")
class ProxiedAuthenticationConverter implements AuthenticationConverter {
  private final ProxiedAuthenticationProperties proxiedAuthenticationProperties;
  private final ObjectMapper objectMapper;

  ProxiedAuthenticationConverter(
    final ProxiedAuthenticationProperties proxiedAuthenticationProperties,
    final ObjectMapper objectMapper
  ) {
    this.proxiedAuthenticationProperties = proxiedAuthenticationProperties;
    this.objectMapper = objectMapper;
  }

  @Override
  @SuppressWarnings("nullness")
  public @Nullable Authentication convert(final HttpServletRequest request) {
    final var proxiedAuthenticationHeaderProperties = proxiedAuthenticationProperties.header();
    final var attributes = EnumerationUtils
      .toList(request.getHeaderNames())
      .stream()
      .map(String::toLowerCase)
      .filter(it -> it.startsWith(proxiedAuthenticationHeaderProperties.prefix()))
      .distinct()
      .flatMap(it -> Optional
        .ofNullable(request.getHeaders(it))
        .map(EnumerationUtils::toList)
        .filter(Predicate.not(List::isEmpty))
        .map(attribute -> attribute.size() == 1 ? attribute.getFirst() : attribute)
        .map(attribute -> Map.entry(
          it.substring(proxiedAuthenticationHeaderProperties
            .prefix()
            .length()),
          attribute))
        .stream())
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    final var username = Optional
      .ofNullable(attributes.remove(proxiedAuthenticationHeaderProperties.username()))
      .map(it -> objectMapper.convertValue(it, String.class))
      .filter(StringUtils::isNotBlank)
      .orElse(null);
    if (username == null) {
      return null;
    }

    final var authorities = Optional
      .ofNullable(attributes.remove(proxiedAuthenticationHeaderProperties.authorities()))
      .stream()
      .flatMap(it -> it instanceof Collection<?> authorityCollection
        ? authorityCollection.stream()
        : Stream.of(it))
      .map(it -> objectMapper.convertValue(it, String.class))
      .map(it -> StringUtils.splitByWholeSeparator(it, proxiedAuthenticationHeaderProperties.authoritiesDelimiter()))
      .flatMap(Arrays::stream)
      .map(authority -> "%s%s".formatted(proxiedAuthenticationProperties.authorityPrefix(), authority))
      .toList();

    return new ProxiedAuthentication(username, authorities, Collections.unmodifiableMap(attributes));
  }
}
