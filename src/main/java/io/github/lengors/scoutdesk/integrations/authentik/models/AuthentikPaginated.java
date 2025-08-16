package io.github.lengors.scoutdesk.integrations.authentik.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.List;

/**
 * Represents a paginated response from Authentik.
 * <p>
 * This record contains pagination information and a list of results. It is used to handle responses that include
 * multiple items, such as users or groups. The pagination object provides details about the current page, total pages,
 * and item counts. The results list contains the actual items returned by the Authentik API.
 *
 * @param pagination the pagination information
 * @param results    the list of results, which can be of any type
 * @param <T>        the type of items in the results list
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthentikPaginated<@NotNull T>(
  @JsonProperty("pagination") @NotNull AuthentikPagination pagination,
  @JsonProperty("results") @NotNull List<T> results
) {

}
