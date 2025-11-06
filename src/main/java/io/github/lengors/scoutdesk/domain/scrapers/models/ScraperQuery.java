package io.github.lengors.scoutdesk.domain.scrapers.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Set;

/**
 * Represents a query for scraping data.
 *
 * @param owner      The owner of the scraper.
 * @param strategies The set of strategies to be used in the scraping process.
 * @param profiles   The set of profiles to be used in the scraping process.
 * @param searchTerm The term to search for during the scraping process.
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScraperQuery(

  @JsonProperty("owner")
  @NotNull
  String owner,

  @JsonProperty("strategies")
  Set<@NotNull @NotBlank String> strategies,

  @JsonProperty("profiles")
  Set<@NotNull @NotBlank String> profiles,

  @JsonProperty("search_term")
  @NotNull
  String searchTerm
) {

}
