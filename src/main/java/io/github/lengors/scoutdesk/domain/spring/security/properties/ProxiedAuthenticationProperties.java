package io.github.lengors.scoutdesk.domain.spring.security.properties;

import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuration properties for defining behavior and attributes related to proxied authentication. This class
 * integrates with Spring Boot's configuration properties mechanism to allow customization of security settings when
 * working with authentication proxies.
 * <p>
 * The primary purpose of this class is to encapsulate configuration for:
 * <ul>
 *   <li>Headers used in authentication requests, via {@link ProxiedAuthenticationHeaderProperties}.</li>
 *   <li>The prefix for authorities, allowing configuration of how roles or scopes are managed.</li>
 * </ul>
 * <p>
 * These properties are mapped using the prefix {@code spring.security.proxy}.
 *
 * @param header          the nested header configuration for proxied authentication
 * @param authorityPrefix the prefix for authority/role strings, used in security contexts
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
@ConfigurationProperties(prefix = ProxiedAuthenticationProperties.PREFIX)
public record ProxiedAuthenticationProperties(
  @NestedConfigurationProperty
  @NotNull
  ProxiedAuthenticationHeaderProperties header,
  @DefaultValue("SCOPE_")
  @NotNull
  String authorityPrefix
) {
  /**
   * The prefix for proxied authentication configuration properties.
   */
  public static final String PREFIX = "spring.security.proxy";

  /**
   * Constructor for creating a new instance of {@link ProxiedAuthenticationProperties}.
   *
   * @param header          the nested header configuration for proxied authentication
   * @param authorityPrefix the prefix for authority/role strings, used in security contexts
   */
  @ConstructorBinding
  public ProxiedAuthenticationProperties {

  }
}
