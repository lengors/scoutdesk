package io.github.lengors.scoutdesk.api.scrapers.models;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

/**
 * Request to scrape a website with specific strategies and search term.
 *
 * @param strategies the set of strategies to use for scraping
 * @param profiles   the set of profiles to use for scraping
 * @param searchTerm the search term to use for scraping
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScraperOwnedRequest(
  @JsonProperty("strategies")
  Set<@NotNull @NotBlank String> strategies,

  @JsonProperty("profiles")
  Set<@NotNull @NotBlank String> profiles,

  @JsonProperty("search_term")
  @NotNull
  @NotBlank
  String searchTerm
) {

}
