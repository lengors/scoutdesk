package io.github.lengors.scoutdesk.integrations.webscout.commands.models;

import io.github.lengors.protoscout.domain.scrapers.models.ScraperRequest;
import io.github.lengors.protoscout.domain.scrapers.models.ScraperResponse;
import io.github.lengors.scoutdesk.domain.commands.models.Command;
import reactor.core.publisher.Flux;

/**
 * Command to scrape data using a request.
 *
 * This command is used to trigger the scraping process based on the provided
 * request parameters.
 *
 * @author lengors
 */
public record ScraperCommand() implements Command<ScraperRequest, Flux<ScraperResponse>> {

}
