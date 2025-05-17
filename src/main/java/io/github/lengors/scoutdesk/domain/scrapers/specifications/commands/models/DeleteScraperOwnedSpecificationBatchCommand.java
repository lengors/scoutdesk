package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models;

import org.checkerframework.checker.nullness.qual.Nullable;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationBatchFilter;

/**
 * Command for deleting a batch of owned scraper specifications using a filter.
 *
 * Used to trigger the removal of multiple specifications matching the filter
 * criteria.
 *
 * @author lengors
 */
public record DeleteScraperOwnedSpecificationBatchCommand()
    implements Command<ScraperOwnedSpecificationBatchFilter, @Nullable Void> {

}
