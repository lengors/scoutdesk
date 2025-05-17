package io.github.lengors.scoutdesk.integrations.webscout.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.lengors.protoscout.domain.scrapers.models.ScraperRequest;
import io.github.lengors.protoscout.domain.scrapers.models.ScraperResponse;
import io.github.lengors.scoutdesk.integrations.webscout.properties.WebscoutClientConnectionDetails;
import reactor.core.publisher.Flux;

/**
 * Client for interacting with the Webscout REST API for scraping data.
 *
 * Provides methods to send scraping requests and receive responses.
 *
 * @author lengors
 */
@Component
public class WebscoutClient {
  private static final Logger LOG = LoggerFactory.getLogger(WebscoutClient.class);
  private static final String BASE_PATH = "api/v1/scrapers";

  private final WebClient webClient;

  WebscoutClient(
      final WebscoutClientConnectionDetails webscoutClientConnectionDetails,
      final WebClient.Builder webClientBuilder) {
    LOG.info("Setting {} up: {}", getClass().getSimpleName(), webscoutClientConnectionDetails.url());
    this.webClient = webClientBuilder
        .baseUrl(webscoutClientConnectionDetails.url())
        .build();
  }

  /**
   * Sends a scraping request to the Webscout API.
   *
   * @param request The scraping request to send
   * @return A Flux of ScraperResponse containing the scraping results
   */
  public Flux<ScraperResponse> scrap(final ScraperRequest request) {
    return webClient
        .post()
        .uri(BASE_PATH)
        .bodyValue(request)
        .retrieve()
        .bodyToFlux(ScraperResponse.class);
  }
}
