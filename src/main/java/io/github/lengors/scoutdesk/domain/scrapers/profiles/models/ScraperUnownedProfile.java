package io.github.lengors.scoutdesk.domain.scrapers.profiles.models;

import java.util.Map;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a scraper profile that is not owned by a user.
 *
 * This class contains the name, specification reference, and input parameters
 * for the profile.
 *
 * @param name          The name of the profile.
 * @param specification The specification reference for the profile.
 * @param inputs        The input parameters for the profile.
 *
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScraperUnownedProfile(
    @JsonProperty("name") @NotNull String name,
    @JsonProperty("specification") @NotNull ScraperOwnedSpecificationReference specification,
    @JsonProperty("inputs") @NotNull Map<@NotNull String, @NotNull String> inputs) implements ScraperNamedProfile {

}
