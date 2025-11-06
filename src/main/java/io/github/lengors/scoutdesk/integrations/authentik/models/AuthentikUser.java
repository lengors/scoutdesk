package io.github.lengors.scoutdesk.integrations.authentik.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.List;
import java.util.UUID;

/**
 * Represents an Authentik user.
 * <p>
 * This record encapsulates the details of a user in Authentik, including their ID, username, name, active status,
 * groups, email, avatar URL, user ID, and UUID. It is used to transfer user information between the application and
 * Authentik.
 *
 * @param id       the unique identifier of the user
 * @param username the username of the user
 * @param name     the full name of the user
 * @param isActive the active status of the user, indicating whether the user is currently active
 * @param groups   the list of groups the user belongs to, represented as a list of {@link AuthentikGroup} objects
 * @param email    the email address of the user, can be null if not provided
 * @param avatar   the URL of the user's avatar image, cannot be null
 * @param uid      the unique identifier of the user in Authentik, cannot be null
 * @param uuid     the universally unique identifier (UUID) of the user, cannot be null
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthentikUser(
  @JsonProperty("pk")
  @NotNull
  Integer id,

  @JsonProperty("username")
  @NotNull
  String username,

  @JsonProperty("name")
  @NotNull
  String name,

  @JsonProperty("is_active")
  Boolean isActive,

  @JsonProperty("groups_obj")
  List<@NotNull AuthentikGroup> groups,

  @JsonProperty("email")
  String email,

  @JsonProperty("avatar")
  @NotNull
  String avatar,

  @JsonProperty("uid")
  @NotNull
  String uid,

  @JsonProperty("uuid")
  @NotNull
  UUID uuid
) {

}
