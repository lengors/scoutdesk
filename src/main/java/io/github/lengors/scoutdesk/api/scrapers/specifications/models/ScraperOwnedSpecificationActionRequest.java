package io.github.lengors.scoutdesk.api.scrapers.specifications.models;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

/**
 * Request model for actions on scraper-owned specifications.
 *
 * Encapsulates the action to be performed on a specification.
 *
 * @param action The action to be performed on the scraper specification
 *
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScraperOwnedSpecificationActionRequest(
    @JsonProperty("action") @NotNull ScraperOwnedSpecificationAction action) {

}
