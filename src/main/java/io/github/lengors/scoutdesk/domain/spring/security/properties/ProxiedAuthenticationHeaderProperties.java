package io.github.lengors.scoutdesk.domain.spring.security.properties;

import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuration properties representing the structure and behavior of HTTP headers used for proxied authentication.
 * This record is part of the configuration for extracting authentication information from incoming HTTP requests in
 * scenarios where an authentication proxy is used.
 * <p>
 * The properties allow for customization of the header prefix, the key for the username, the key for authorities, and
 * the delimiter to parse multiple authorities within the header. The configuration is designed to integrate seamlessly
 * into the broader security setup of the application.
 *
 * @param prefix               the prefix used for authentication headers
 * @param username             the key in the header for the username
 * @param authorities          the key in the header for the authorities or roles
 * @param authoritiesDelimiter the delimiter used to separate multiple authorities in the header value
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
public record ProxiedAuthenticationHeaderProperties(
  @NotNull
  String prefix,
  @DefaultValue("username")
  @NotNull
  String username,
  @DefaultValue("scope")
  @NotNull
  String authorities,
  @DefaultValue("|")
  @NotNull
  String authoritiesDelimiter
) {

  /**
   * Constructor for creating a new instance of {@link ProxiedAuthenticationHeaderProperties}.
   *
   * @param prefix               the prefix used for authentication headers
   * @param username             the key in the header for the username
   * @param authorities          the key in the header for the authorities or roles
   * @param authoritiesDelimiter the delimiter used to separate multiple authorities in the header value
   */
  @ConstructorBinding
  public ProxiedAuthenticationHeaderProperties {

  }
}
