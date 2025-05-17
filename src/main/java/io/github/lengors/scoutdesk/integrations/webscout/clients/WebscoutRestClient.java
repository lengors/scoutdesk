package io.github.lengors.scoutdesk.integrations.webscout.clients;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.integrations.webscout.properties.WebscoutClientConnectionDetails;

/**
 * Client for interacting with the Webscout REST API for scraper specifications.
 *
 * Provides methods to find, save, and delete {@link ScraperSpecification}
 * resources.
 *
 * @author lengors
 */
@Component
public class WebscoutRestClient {

  @SuppressWarnings("LineLength")
  private static final ParameterizedTypeReference<ScraperSpecification> TYPE_REFERENCE = new ParameterizedTypeReference<>() {
  };

  @SuppressWarnings("LineLength")
  private static final ParameterizedTypeReference<List<ScraperSpecification>> TYPE_REFERENCE_LIST = new ParameterizedTypeReference<>() {
  };

  private static final Logger LOG = LoggerFactory.getLogger(WebscoutRestClient.class);

  private static final String BASE_PATH = "api/v1/scrapers/specifications";

  private final RestClient restClient;

  WebscoutRestClient(
      final WebscoutClientConnectionDetails webscoutClientConnectionDetails,
      final RestClient.Builder restClientBuilder) {
    LOG.info("Setting {} up: {}", getClass().getSimpleName(), webscoutClientConnectionDetails.url());
    this.restClient = restClientBuilder
        .baseUrl(webscoutClientConnectionDetails.url())
        .build();
  }

  /**
   * Deletes a scraper specification by its name.
   *
   * @param name The name of the scraper specification to delete
   * @return The deleted scraper specification
   */
  public ScraperSpecification delete(final String name) {
    return Optional
        .ofNullable(restClient
            .delete()
            .uri(String.format("%s/%s", BASE_PATH, name))
            .retrieve()
            .body(TYPE_REFERENCE))
        .orElseThrow();
  }

  /**
   * Deletes all scraper specifications.
   *
   * @return A list of deleted scraper specifications
   */
  public List<ScraperSpecification> deleteAll() {
    return Optional
        .ofNullable(restClient
            .delete()
            .uri(BASE_PATH)
            .retrieve()
            .body(TYPE_REFERENCE_LIST))
        .orElseThrow();
  }

  /**
   * Deletes all scraper specifications by their names.
   *
   * @param names The names of the scraper specifications to delete
   * @return A list of deleted scraper specifications
   */
  public List<ScraperSpecification> deleteAll(final Collection<String> names) {
    return Optional
        .ofNullable(restClient
            .delete()
            .uri(uriBuilder -> uriBuilder
                .path(BASE_PATH)
                .queryParam("name", names)
                .build())
            .retrieve()
            .body(TYPE_REFERENCE_LIST))
        .orElseThrow();
  }

  /**
   * Finds a scraper specification by its name.
   *
   * @param name The name of the scraper specification to find
   * @return The found scraper specification
   */
  public ScraperSpecification find(final String name) {
    return Optional
        .ofNullable(restClient
            .get()
            .uri(String.format("%s/%s", BASE_PATH, name))
            .retrieve()
            .body(TYPE_REFERENCE))
        .orElseThrow();
  }

  /**
   * Finds all scraper specifications.
   *
   * @param names The names of the scraper specifications to find
   * @return A list of found scraper specifications
   */
  public List<ScraperSpecification> findAll(final Collection<String> names) {
    return Optional
        .ofNullable(restClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .path(BASE_PATH)
                .queryParam("name", names)
                .build())
            .retrieve()
            .body(TYPE_REFERENCE_LIST))
        .orElseThrow();
  }

  /**
   * Saves a scraper specification.
   *
   * @param scraperSpecification The scraper specification to save
   * @return The saved scraper specification
   */
  public ScraperSpecification save(final ScraperSpecification scraperSpecification) {
    return Optional
        .ofNullable(restClient
            .put()
            .uri(BASE_PATH)
            .body(scraperSpecification)
            .retrieve()
            .body(TYPE_REFERENCE))
        .orElseThrow();
  }
}
