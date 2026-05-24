package io.github.lengors.scoutdesk.domain.spring.security.properties;

import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

/**
 * Configuration properties for mapping user attributes in a proxied authentication context. This record defines the
 * mapping between the incoming authentication data and the application's internal user attributes.
 * <p>
 * Through these properties, the mapping of user-specific details such as the username, name, email, and avatar can be
 * customized to align with the structure of the authentication data provided by the proxy.
 * <p>
 * These properties are mapped using the prefix {@code spring.security.proxy.user-mapping}, as defined in
 * {@link ProxiedAuthenticationProperties}.
 * <p>
 * Each property represents a corresponding user attribute and is expressed as a list of possible mappings to
 * accommodate flexibility when processing potentially varying attribute names in proxied authentication headers.
 *
 * @param username the list of attribute names to map to the user's username
 * @param name     the list of attribute names to map to the user's name
 * @param email    the list of attribute names to map to the user's email
 * @param avatar   the list of attribute names to map to the user's avatar
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
@ConfigurationProperties(prefix = ProxiedAuthenticationProperties.PREFIX + ".user-mapping")
public record ProxiedAuthenticationUserMappingProperties(
  @DefaultValue("username")
  @NotNull
  List<@NotNull String> username,
  @DefaultValue("name")
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
   * Constructor for creating a new instance of {@link ProxiedAuthenticationUserMappingProperties}.
   *
   * @param username the list of attribute names to map to the user's username
   * @param name     the list of attribute names to map to the user's name
   * @param email    the list of attribute names to map to the user's email
   * @param avatar   the list of attribute names to map to the user's avatar
   */
  @ConstructorBinding
  public ProxiedAuthenticationUserMappingProperties {

  }
}
