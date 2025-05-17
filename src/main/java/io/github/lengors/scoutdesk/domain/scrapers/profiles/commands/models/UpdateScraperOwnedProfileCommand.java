package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;

/**
 * Command for updating a scraper owned profile.
 *
 * This command is used to update a {@link ScraperOwnedProfile} instance.
 *
 * @author lengors
 */
public record UpdateScraperOwnedProfileCommand() implements Command<ScraperOwnedProfile, ScraperOwnedProfile> {

}
