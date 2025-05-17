package io.github.lengors.scoutdesk.api.scrapers.profiles.models;

import java.util.Map;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperNamelessProfile;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a partial profile for a scraper, which includes the specification
 * reference and input parameters.
 *
 * @param specification The specification reference for the scraper.
 * @param inputs        The input parameters for the scraper.
 *
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScraperPartialProfile(
    @JsonProperty("specification") @NotNull ScraperOwnedSpecificationReference specification,
    @JsonProperty("inputs") @NotNull Map<@NotNull String, @NotNull String> inputs) implements ScraperNamelessProfile {

}
