package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models;

import java.util.List;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;

/**
 * Command to delete a batch of scraper-owned specification entities.
 *
 * @author lengors
 */
public record DeleteScraperOwnedSpecificationEntityBatchCommand()
    implements Command<List<ScraperOwnedSpecificationEntity>, Void> {

}
