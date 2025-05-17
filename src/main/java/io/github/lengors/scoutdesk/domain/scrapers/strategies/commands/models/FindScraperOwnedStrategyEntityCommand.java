package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyEntity;

/**
 * Command to find a scraper-owned strategy entity.
 *
 * This command is used to retrieve a single strategy entity based on the
 * specified filter criteria.
 *
 * @author lengors
 */
public record FindScraperOwnedStrategyEntityCommand()
    implements Command<ScraperOwnedStrategyFilter, ScraperOwnedStrategyEntity> {

}
