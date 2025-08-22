package io.github.lengors.scoutdesk.domain.scrapers.profiles.models;

import io.github.lengors.scoutdesk.domain.persistence.EntityReferrer;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import jakarta.validation.constraints.NotNull;

/**
 * Represents a reference to a scraper profile owned by a user.
 * <p>
 * This interface contains the owner and name of the profile.
 *
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
public interface ScraperOwnedProfileReferrer extends EntityReferrer<@NotNull ScraperOwnedProfileEntity> {

}
