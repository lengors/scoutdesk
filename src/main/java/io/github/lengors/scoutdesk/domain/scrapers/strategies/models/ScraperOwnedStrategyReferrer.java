package io.github.lengors.scoutdesk.domain.scrapers.strategies.models;

import io.github.lengors.scoutdesk.domain.persistence.EntityReferrer;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import jakarta.validation.constraints.NotNull;

/**
 * Represents a reference to a scraper strategy owned by a user.
 * <p>
 * This interface contains the owner and name of the strategy.
 *
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
public interface ScraperOwnedStrategyReferrer extends EntityReferrer<@NotNull ScraperOwnedStrategyEntity> {

}
