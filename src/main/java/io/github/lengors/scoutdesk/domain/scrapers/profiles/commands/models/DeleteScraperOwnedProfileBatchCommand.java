package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models;

import org.checkerframework.checker.nullness.qual.Nullable;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchFilter;

/**
 * Command to delete a batch of scraper-owned profiles.
 *
 * This command is used to trigger the deletion of multiple profiles that match
 * the specified filter criteria.
 *
 * @author lengors
 */
public record DeleteScraperOwnedProfileBatchCommand()
    implements Command<ScraperOwnedProfileBatchFilter, @Nullable Void> {

}
