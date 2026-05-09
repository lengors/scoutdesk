package io.github.lengors.scoutdesk.api.scrapers.specifications.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import io.github.lengors.scoutdesk.domain.persistence.converters.EntityNotFoundExceptionReportConverter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationByReferrerAndStatusNotFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import org.apache.commons.lang3.tuple.Pair;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.util.NullnessUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;

import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories.ScraperOwnedSpecificationRepository;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories.ScraperOwnedStrategyRepository;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;
import io.github.lengors.scoutdesk.testing.utilities.ResourceUtils;
import io.github.lengors.scoutdesk.testing.utilities.TestSuite;

@TestSuite.Defaults
record SharedScraperOwnedSpecificationRestControllerTest(
  @Autowired MockMvc mockMvc,
  @Autowired ResourceUtils resourceUtils,
  @Autowired WebscoutRestClient webscoutRestClient,
  @Autowired PlatformTransactionManager platformTransactionManager,
  @Autowired ScraperOwnedProfileRepository scraperOwnedProfileRepository,
  @Autowired ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository,
  @Autowired ScraperOwnedStrategyRepository scraperOwnedStrategyRepository
) implements TestSuite {

  @SuppressWarnings("unused")
  private static final TestOption[] TEST_OPTIONS = new TestOption[] {
    new TestOption("Test-Spec-0", "tester-0", true, true, List.of(
      Pair.of("$.length()", 1),
      Pair.of("$[0].owner", "tester-0"),
      Pair.of("$[0].specification.name", "test-specification-0"))),

    new TestOption("Test-Spec-0", "tester-0", true, false, List.of(
      Pair.of("$.length()", 2),
      Pair.of("$[0].owner", "tester-0"),
      Pair.of("$[0].specification.name", "test-specification-0"),
      Pair.of("$[1].owner", "tester-0"),
      Pair.of("$[1].specification.name", "test-specification-2"))),

    new TestOption("Test-Spec-0", "tester-0", false, true, List.of(
      Pair.of("$.length()", 0))),

    new TestOption("test-spec-0", "tester-0", false, true, List.of(
      Pair.of("$.length()", 1),
      Pair.of("$[0].owner", "tester-0"),
      Pair.of("$[0].specification.name", "test-specification-0"))),

    new TestOption("test-spec-", "tester-0", true, true, List.of(
      Pair.of("$.length()", 2),
      Pair.of("$[0].owner", "tester-0"),
      Pair.of("$[0].specification.name", "test-specification-0"),
      Pair.of("$[1].owner", "tester-0"),
      Pair.of("$[1].specification.name", "test-specification-2"))),

    new TestOption("test-spec-", null, true, true, List.of(
      Pair.of("$.length()", 3),
      Pair.of("$[0].owner", "tester-0"),
      Pair.of("$[0].specification.name", "test-specification-0"),
      Pair.of("$[1].owner", "tester-0"),
      Pair.of("$[1].specification.name", "test-specification-2"),
      Pair.of("$[2].owner", "tester-1"),
      Pair.of("$[2].specification.name", "test-specification-0"))),

    new TestOption("Test-Spec-0", "tester-0", null, true, List.of(
      Pair.of("$.length()", 1),
      Pair.of("$[0].owner", "tester-0"),
      Pair.of("$[0].specification.name", "test-specification-0"))),

    new TestOption("test-spec-0", "tester-0", null, null, List.of(
      Pair.of("$.length()", 2),
      Pair.of("$[0].owner", "tester-0"),
      Pair.of("$[0].specification.name", "test-specification-0"),
      Pair.of("$[1].owner", "tester-0"),
      Pair.of("$[1].specification.name", "test-specification-2"))),

    new TestOption(null, "tester-0", null, null, List.of(
      Pair.of("$.length()", 2),
      Pair.of("$[0].owner", "tester-0"),
      Pair.of("$[0].specification.name", "test-specification-0"),
      Pair.of("$[1].owner", "tester-0"),
      Pair.of("$[1].specification.name", "test-specification-2"))),

    new TestOption(null, null, null, null, List.of(
      Pair.of("$.length()", 3),
      Pair.of("$[0].owner", "tester-0"),
      Pair.of("$[0].specification.name", "test-specification-0"),
      Pair.of("$[1].owner", "tester-0"),
      Pair.of("$[1].specification.name", "test-specification-2"),
      Pair.of("$[2].owner", "tester-1"),
      Pair.of("$[2].specification.name", "test-specification-0"))),
  };

  @ParameterizedTest
  @FieldSource("TEST_OPTIONS")
  void givenValidUserAndParamsWhenFindSpecificationsThenReturnExpectedResults(final TestOption testOption)
    throws Exception {

    var request = get("/api/v1/shared/scrapers/specifications");
    if (testOption.query() != null) {
      request = request.param("query", testOption.query());
    }
    if (testOption.owner() != null) {
      request = request.param("owner", testOption.owner());
    }
    if (testOption.ignoreCase() != null) {
      request = request.param("ignore-case", String.valueOf(testOption.ignoreCase()));
    }
    if (testOption.strictModeEnabled() != null) {
      request = request.param("strict-mode-enabled", String.valueOf(testOption.strictModeEnabled()));
    }
    var mockMvcResultActions = mockMvc
      .perform(request
        .header("X-authentik-username", "tester-2"))
      .andExpect(status().isOk());
    for (final var expectedResult : testOption.expectedResults()) {
      mockMvcResultActions = mockMvcResultActions
        .andExpect(jsonPath(expectedResult.getLeft()).value(expectedResult.getRight()));
    }
  }

  @Test
  void givenUserWithForbiddenGroupWhenFindSpecificationsThenReturnForbidden() throws Exception {
    mockMvc
      .perform(get("/api/v1/shared/scrapers/specifications")
        .header("X-authentik-username", "other"))
      .andExpect(status().isForbidden());
  }

  @Test
  void givenNoAuthWhenFindSpecificationsThenReturnUnauthorized() throws Exception {
    mockMvc
      .perform(get("/api/v1/shared/scrapers/specifications"))
      .andExpect(status().isUnauthorized());
  }

  @Test
  void givenValidUserAndParamsWhenFindSpecificationThenReturnExpectedResults() throws Exception {
    mockMvc
      .perform(
        get("/api/v1/shared/scrapers/specifications")
          .param("name", "test-specification-0")
          .param("owner", "tester-0")
          .header("X-authentik-username", "tester-2"))
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.owner").value("tester-0"))
      .andExpect(jsonPath("$.specification.name").value("test-specification-0"))
      .andExpect(jsonPath("$.status").value("active"));
  }

  @Test
  void givenIncorrectOwnerWhenFindSpecificationThenReturnNotFound() throws Exception {
    mockMvc
      .perform(
        get("/api/v1/shared/scrapers/specifications")
          .param("name", "test-specification-0")
          .param("owner", "tester-2")
          .header("X-authentik-username", "tester-1"))
      .andExpect(status().isNotFound())
      .andExpect(content().string(
        EntityNotFoundExceptionReportConverter.MESSAGE.formatted(
          NullnessUtil
            .castNonNull(ScraperOwnedSpecificationEntity.class)
            .getSimpleName(),
          new ScraperOwnedSpecificationByReferrerAndStatusNotFilter(
            new ScraperOwnedSpecificationReference("tester-2", "test-specification-0")))));
  }

  @Test
  void givenIncorrectNameWhenFindSpecificationThenReturnNotFound() throws Exception {
    mockMvc
      .perform(
        get("/api/v1/shared/scrapers/specifications")
          .param("name", "test-specification-3")
          .param("owner", "tester-0")
          .header("X-authentik-username", "tester-2"))
      .andExpect(status().isNotFound())
      .andExpect(content().string(
        EntityNotFoundExceptionReportConverter.MESSAGE.formatted(
          NullnessUtil
            .castNonNull(ScraperOwnedSpecificationEntity.class)
            .getSimpleName(),
          new ScraperOwnedSpecificationByReferrerAndStatusNotFilter(
            new ScraperOwnedSpecificationReference("tester-0", "test-specification-3")))));
  }

  @Test
  void givenNoAuthWhenFindSpecificationThenReturnExpectedResults() throws Exception {
    mockMvc
      .perform(
        get("/api/v1/shared/scrapers/specifications")
          .param("name", "test-specification-0")
          .param("owner", "tester-0"))
      .andExpect(status().isUnauthorized());
  }

  @Test
  void givenUserWithForbiddenGroupWhenFindSpecificationThenReturnExpectedResults() throws Exception {
    mockMvc
      .perform(
        get("/api/v1/shared/scrapers/specifications")
          .param("name", "test-specification-0")
          .param("owner", "tester-0")
          .header("X-authentik-username", "other"))
      .andExpect(status().isForbidden());
  }

  private record TestOption(
    @Nullable String query,
    @Nullable String owner,
    @Nullable Boolean ignoreCase,
    @Nullable Boolean strictModeEnabled,
    List<Pair<String, Object>> expectedResults
  ) {
  }
}
