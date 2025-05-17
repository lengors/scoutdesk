package io.github.lengors.scoutdesk.domain.scrapers.commands.models;

import io.github.lengors.protoscout.domain.scrapers.models.ScraperResponse;
import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.models.ScraperQuery;
import reactor.core.publisher.Flux;

/**
 * Command to scrape data using a query.
 *
 * This command is used to trigger the scraping process based on the provided
 * query parameters.
 *
 * @author lengors
 */
public record ScraperOwnedCommand() implements Command<ScraperQuery, Flux<ScraperResponse>> {

}
