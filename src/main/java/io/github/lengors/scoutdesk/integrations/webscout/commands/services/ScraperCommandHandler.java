package io.github.lengors.scoutdesk.integrations.webscout.commands.services;

import org.springframework.stereotype.Service;

import io.github.lengors.protoscout.domain.scrapers.models.ScraperRequest;
import io.github.lengors.protoscout.domain.scrapers.models.ScraperResponse;
import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutClient;
import io.github.lengors.scoutdesk.integrations.webscout.commands.models.ScraperCommand;
import reactor.core.publisher.Flux;

@Service
class ScraperCommandHandler implements CommandHandler<ScraperCommand, ScraperRequest, Flux<ScraperResponse>> {
  private final WebscoutClient webscoutClient;

  ScraperCommandHandler(final WebscoutClient webscoutClient) {
    this.webscoutClient = webscoutClient;
  }

  @Override
  public Flux<ScraperResponse> handle(final ScraperCommand command, final ScraperRequest input) {
    return webscoutClient.scrap(input);
  }
}
