package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;

/**
 * Command for finding a scraper owned profile.
 *
 * This command is used to retrieve a {@link ScraperOwnedProfile} instance based
 * on the provided filter criteria.
 *
 * @author lengors
 */
public record FindScraperOwnedProfileCommand() implements Command<ScraperOwnedProfileFilter, ScraperOwnedProfile> {

}
