package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;

/**
 * Command to delete a single scraper-owned specification entity.
 *
 * @author lengors
 */
public record DeleteScraperOwnedSpecificationEntityCommand() implements Command<ScraperOwnedSpecificationEntity, Void> {

}
