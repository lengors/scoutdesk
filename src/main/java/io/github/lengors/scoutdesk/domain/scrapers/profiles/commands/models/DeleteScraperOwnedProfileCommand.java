package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileFilter;

/**
 * Command to delete a scraper-owned profile.
 *
 * This command is used to trigger the deletion of a profile that matches the
 * specified filter criteria.
 *
 * @author lengors
 */
public record DeleteScraperOwnedProfileCommand() implements Command<ScraperOwnedProfileFilter, Void> {

}
