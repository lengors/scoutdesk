package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models;

import java.util.List;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;

/**
 * Command for finding a batch of scraper owned profiles.
 *
 * This command is used to retrieve a list of {@link ScraperOwnedProfile}
 * instances based on the provided filter criteria.
 *
 * @author lengors
 */
public record FindScraperOwnedProfileBatchCommand()
    implements Command<ScraperOwnedProfileBatchFilter, List<ScraperOwnedProfile>> {

}
