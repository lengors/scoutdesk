package io.github.lengors.scoutdesk.domain.spring.security.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Collection;

/**
 * User information model. This record is used to encapsulate user details such as username, name, roles, email, and
 * avatar. It is typically used in API responses to provide user information to clients.
 *
 * @param username the username of the user
 * @param name     the full name of the user
 * @param roles    the roles assigned to the user
 * @param email    the email address of the user, can be null
 * @param avatar   the URL of the user's avatar image
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record User(
  @JsonProperty("username") @NotNull String username,
  @JsonProperty("name") @NotNull String name,
  @JsonProperty("roles") @NotNull Collection<@NotNull UserRole> roles,
  @JsonProperty("email") String email,
  @JsonProperty("avatar") @NotNull String avatar
) {

}
