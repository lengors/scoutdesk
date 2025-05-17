package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;

/**
 * Command for saving a scraper owned profile.
 *
 * This command is used to save a {@link ScraperOwnedProfile} instance.
 *
 * @author lengors
 */
public record SaveScraperOwnedProfileCommand() implements Command<ScraperOwnedProfile, ScraperOwnedProfile> {

}
