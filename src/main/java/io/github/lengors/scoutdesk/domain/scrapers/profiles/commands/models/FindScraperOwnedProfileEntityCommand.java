package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;

/**
 * Command to find a scraper-owned profile entity.
 *
 * This command is used to retrieve a single profile entity based on the
 * specified filter criteria.
 *
 * @author lengors
 */
public record FindScraperOwnedProfileEntityCommand()
    implements Command<ScraperOwnedProfileFilter, ScraperOwnedProfileEntity> {

}
