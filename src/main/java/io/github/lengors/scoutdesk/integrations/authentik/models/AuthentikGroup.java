package io.github.lengors.scoutdesk.integrations.authentik.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.UUID;

/**
 * Represents an Authentik group.
 * <p>
 * This record holds the details of a group in Authentik, including its ID, name, and UUID. It is used to manage group
 * information within the application.
 *
 * @param id   the unique identifier of the group
 * @param name the name of the group
 * @param uuid the universally unique identifier of the group
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthentikGroup(
  @JsonProperty("num_pk")
  @NotNull
  Integer id,

  @JsonProperty("name")
  @NotNull
  String name,

  @JsonProperty("pk")
  @NotNull
  UUID uuid
) {
}
