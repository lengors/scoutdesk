package io.github.lengors.scoutdesk.api.scrapers.controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.PlatformTransactionManager;

import io.github.lengors.protoscout.domain.scrapers.models.ScraperResponse;
import io.github.lengors.protoscout.domain.scrapers.models.ScraperResponseResult;
import io.github.lengors.protoscout.domain.scrapers.models.ScraperResponseResultBrand;
import io.github.lengors.protoscout.domain.scrapers.models.ScraperResponseResultPrice;
import io.github.lengors.scoutdesk.api.scrapers.models.ScraperOwnedRequest;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories.ScraperOwnedSpecificationRepository;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories.ScraperOwnedStrategyRepository;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;
import io.github.lengors.scoutdesk.testing.utilities.ResourceUtils;
import io.github.lengors.scoutdesk.testing.utilities.TestSuite;

@TestSuite.Defaults
class ScraperOwnedControllerTest implements TestSuite {
  @SuppressWarnings("unused")
  private static final String[] TEST_GROUPS = new String[] {
      "Scoutdesk Users", "Scoutdesk Developers", "Scoutdesk Admins"
  };

  private final ResourceUtils resourceUtils;
  private final WebscoutRestClient webscoutRestClient;
  private final PlatformTransactionManager platformTransactionManager;
  private final ScraperOwnedProfileRepository scraperOwnedProfileRepository;
  private final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository;
  private final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository;
  private final WebTestClient webTestClient;

  ScraperOwnedControllerTest(
      @Autowired final ResourceUtils resourceUtils,
      @Autowired final WebscoutRestClient webscoutRestClient,
      @Autowired final PlatformTransactionManager platformTransactionManager,
      @Autowired final ScraperOwnedProfileRepository scraperOwnedProfileRepository,
      @Autowired final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository,
      @Autowired final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository,
      @Autowired final WebTestClient webTestClient) {
    this.resourceUtils = resourceUtils;
    this.webscoutRestClient = webscoutRestClient;
    this.platformTransactionManager = platformTransactionManager;
    this.scraperOwnedProfileRepository = scraperOwnedProfileRepository;
    this.scraperOwnedSpecificationRepository = scraperOwnedSpecificationRepository;
    this.scraperOwnedStrategyRepository = scraperOwnedStrategyRepository;
    this.webTestClient = webTestClient;
  }

  @Override
  public PlatformTransactionManager getPlatformTransactionManager() {
    return platformTransactionManager;
  }

  @Override
  public ResourceUtils getResourceUtils() {
    return resourceUtils;
  }

  @Override
  public ScraperOwnedProfileRepository getScraperOwnedProfileRepository() {
    return scraperOwnedProfileRepository;
  }

  @Override
  public ScraperOwnedSpecificationRepository getScraperOwnedSpecificationRepository() {
    return scraperOwnedSpecificationRepository;
  }

  @Override
  public ScraperOwnedStrategyRepository getScraperOwnedStrategyRepository() {
    return scraperOwnedStrategyRepository;
  }

  @Override
  public WebscoutRestClient getWebscoutRestClient() {
    return webscoutRestClient;
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void shouldCorrectlyScrape(final String testGroup) {
    webTestClient
        .post()
        .uri("/api/v1/scrapers")
        .header("X-authentik-username", "tester-x")
        .header("X-authentik-groups", testGroup)
        .bodyValue(new ScraperOwnedRequest(Set.of("test-strategy-x", "test-strategy-y"), "test-term"))
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.TEXT_EVENT_STREAM)
        .expectBodyList(ScraperResponse.class)
        .value(response -> {
          final var expectedCount = 3;
          Assertions.assertEquals(expectedCount, response.size());
          Assertions.assertTrue(response.containsAll(
              Arrays.asList(
                  new ScraperResponseResult(
                      "https://example.com",
                      "tester-1-test-specification-1",
                      "TEST-DESCRIPTION-X-TEST-TERM",
                      new ScraperResponseResultBrand("test-brand-description-x-test-term", null),
                      new ScraperResponseResultPrice("0.5", "EUR"),
                      null,
                      Collections.emptyList(),
                      null,
                      null,
                      null,
                      null,
                      Collections.emptyList()),
                  new ScraperResponseResult(
                      "https://example.com",
                      "tester-1-test-specification-1",
                      "TEST-DESCRIPTION-Y-TEST-TERM",
                      new ScraperResponseResultBrand("test-brand-description-y-test-term", null),
                      new ScraperResponseResultPrice("0.5", "EUR"),
                      null,
                      Collections.emptyList(),
                      null,
                      null,
                      null,
                      null,
                      Collections.emptyList()),
                  new ScraperResponseResult(
                      "https://example.com",
                      "tester-1-test-specification-1",
                      "TEST-DESCRIPTION-Z-TEST-TERM",
                      new ScraperResponseResultBrand("test-brand-description-z-test-term", null),
                      new ScraperResponseResultPrice("0.5", "EUR"),
                      null,
                      Collections.emptyList(),
                      null,
                      null,
                      null,
                      null,
                      Collections.emptyList()))));
        });
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void shouldFailToScrapeDueToMissingStrategy(final String testGroup) {
    webTestClient
        .post()
        .uri("/api/v1/scrapers")
        .header("X-authentik-username", "tester-x")
        .header("X-authentik-groups", testGroup)
        .bodyValue(new ScraperOwnedRequest(Set.of("test-strategy-a"), "test-term"))
        .exchange()
        .expectStatus()
        .isNotFound();
  }

  @Test
  void shouldFailToScrapeWithUnauthorized() {
    webTestClient
        .post()
        .uri("/api/v1/scrapers")
        .bodyValue(new ScraperOwnedRequest(Set.of("test-strategy-x", "test-strategy-y"), "test-term"))
        .exchange()
        .expectStatus()
        .isUnauthorized();
  }

  @Test
  void shouldFailToScrapeWithForbidden() {
    webTestClient
        .post()
        .uri("/api/v1/scrapers")
        .header("X-authentik-username", "tester-x")
        .header("X-authentik-groups", "Other")
        .bodyValue(new ScraperOwnedRequest(Set.of("test-strategy-x", "test-strategy-y"), "test-term"))
        .exchange()
        .expectStatus()
        .isForbidden();
  }
}
