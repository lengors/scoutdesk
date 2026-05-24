package io.github.lengors.scoutdesk.domain.spring.security.properties;

import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

/**
 * Configuration properties for mapping user-related attributes in an OAuth2 Resource Server context. This class is used
 * to define mappings for extracting user attributes such as username, name, email, and avatar from OAuth2 tokens or
 * authentication sources.
 * <p>
 * The properties are configurable via the prefix: {@code spring.security.oauth2.resource-server.user-mapping}. Each
 * attribute can be mapped to multiple identity claims, allowing flexibility in resolving user details from various
 * identity providers.
 * <p>
 * This class implements the {@link UserMappingProperties} interface which provides accessors for the configured
 * mappings.
 *
 * @param username the list of claims to map to the username attribute
 * @param name     the list of claims to map to the name attribute
 * @param email    the list of claims to map to the email attribute
 * @param avatar   the list of claims to map to the avatar attribute
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
@ConfigurationProperties(prefix = "spring.security.oauth2.resource-server.user-mapping")
public record OAuth2ResourceServerUserMappingProperties(
  @DefaultValue({"preferred_username", "nickname"})
  @NotNull
  List<@NotNull String> username,
  @DefaultValue({"name", "given_name"})
  @NotNull
  List<@NotNull String> name,
  @DefaultValue("email")
  @NotNull
  List<@NotNull String> email,
  @DefaultValue("avatar")
  @NotNull
  List<@NotNull String> avatar
) implements UserMappingProperties {

  /**
   * Constructor for creating a new instance of {@link OAuth2ResourceServerUserMappingProperties}.
   *
   * @param username the list of claims to map to the username attribute
   * @param name     the list of claims to map to the name attribute
   * @param email    the list of claims to map to the email attribute
   * @param avatar   the list of claims to map to the avatar attribute
   */
  @ConstructorBinding
  public OAuth2ResourceServerUserMappingProperties {

  }
}
