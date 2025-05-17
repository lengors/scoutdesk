package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models;

import java.util.List;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;

/**
 * Command to find a batch of scraper-owned profile entities.
 *
 * This command is used to retrieve a list of profile entities based on the
 * specified filter criteria.
 *
 * @author lengors
 */
public record FindScraperOwnedProfileEntityBatchCommand()
    implements Command<ScraperOwnedProfileBatchFilter, List<ScraperOwnedProfileEntity>> {

}
