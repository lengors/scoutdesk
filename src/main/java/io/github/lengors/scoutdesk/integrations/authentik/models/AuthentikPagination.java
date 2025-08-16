package io.github.lengors.scoutdesk.integrations.authentik.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * Represents pagination information for Authentik API responses.
 * <p>
 * This record contains the next page number for paginated responses. It is used to navigate through multiple pages of
 * results.
 *
 * @param next The next page number in the pagination sequence. If there are no more pages, this value may be null.
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthentikPagination(
  @JsonProperty("next") @NotNull Integer next
) {

}
