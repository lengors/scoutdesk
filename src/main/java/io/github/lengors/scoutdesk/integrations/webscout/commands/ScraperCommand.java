package io.github.lengors.scoutdesk.integrations.webscout.commands;

import io.github.lengors.protoscout.domain.scrapers.models.ScraperRequest;
import io.github.lengors.protoscout.domain.scrapers.models.ScraperResponse;
import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Command to scrape data using a request.
 * <p>
 * This command is used to trigger the scraping process based on the provided request parameters.
 *
 * @author lengors
 */
public record ScraperCommand() implements Command<ScraperRequest, Flux<ScraperResponse>> {
  @Service
  static class Handler implements CommandHandler<ScraperCommand, ScraperRequest, Flux<ScraperResponse>> {
    private final WebscoutClient webscoutClient;

    Handler(final WebscoutClient webscoutClient) {
      this.webscoutClient = webscoutClient;
    }

    @Override
    public Flux<ScraperResponse> handle(final ScraperCommand command, final ScraperRequest input) {
      return webscoutClient.scrap(input);
    }
  }
}
