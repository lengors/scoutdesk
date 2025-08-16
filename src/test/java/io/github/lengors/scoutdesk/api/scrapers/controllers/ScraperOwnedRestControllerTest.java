package io.github.lengors.scoutdesk.api.scrapers.controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
record ScraperOwnedRestControllerTest(
  @Autowired ResourceUtils resourceUtils,
  @Autowired WebscoutRestClient webscoutRestClient,
  @Autowired PlatformTransactionManager platformTransactionManager,
  @Autowired ScraperOwnedProfileRepository scraperOwnedProfileRepository,
  @Autowired ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository,
  @Autowired ScraperOwnedStrategyRepository scraperOwnedStrategyRepository,
  @Autowired WebTestClient webTestClient
) implements TestSuite {
  @Test
  void shouldCorrectlyScrape() {
    webTestClient
      .post()
      .uri("/api/v1/scrapers")
      .header("X-authentik-username", "tester-x")
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

  @Test
  void shouldFailToScrapeDueToMissingStrategy() {
    webTestClient
      .post()
      .uri("/api/v1/scrapers")
      .header("X-authentik-username", "tester-x")
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
      .header("X-authentik-username", "other")
      .bodyValue(new ScraperOwnedRequest(Set.of("test-strategy-x", "test-strategy-y"), "test-term"))
      .exchange()
      .expectStatus()
      .isForbidden();
  }
}
