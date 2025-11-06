package io.github.lengors.scoutdesk.domain.scrapers.profiles.models;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a scraper profile that is not owned by a user.
 * <p>
 * This class contains the name, specification reference, and input parameters for the profile.
 *
 * @param name          The name of the profile.
 * @param specification The specification reference for the profile.
 * @param inputs        The input parameters for the profile.
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScraperUnownedProfile(
  @JsonProperty("name")
  @NotNull
  @Pattern(regexp = "^[^/\\s]+$")
  String name,

  @JsonProperty("specification")
  @NotNull
  ScraperOwnedSpecificationReference specification,

  @JsonProperty("inputs")
  Map<@NotNull String, @NotNull @NotBlank String> inputs
) implements ScraperNamedProfile {

}
