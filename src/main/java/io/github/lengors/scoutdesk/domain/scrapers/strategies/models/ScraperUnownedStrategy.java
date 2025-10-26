package io.github.lengors.scoutdesk.domain.scrapers.strategies.models;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

/**
 * Represents a scraper strategy that is not owned by a user.
 * <p>
 * This class contains the name and profiles for the strategy.
 *
 * @param name     The name of the strategy.
 * @param profiles The profiles associated with the strategy.
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScraperUnownedStrategy(
  @JsonProperty("name")
  @NotNull
  @Pattern(regexp = "^[^/\\s]+$")
  String name,

  @JsonProperty("profiles")
  @NotNull
  Set<@NotNull @NotBlank String> profiles
) implements ScraperNamedStrategy {

}
