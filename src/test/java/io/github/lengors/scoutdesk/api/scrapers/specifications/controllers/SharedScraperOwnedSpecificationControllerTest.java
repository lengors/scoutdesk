package io.github.lengors.scoutdesk.api.scrapers.specifications.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;

import io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories.ScraperOwnedSpecificationRepository;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;
import io.github.lengors.scoutdesk.testing.utilities.ResourceUtils;
import io.github.lengors.scoutdesk.testing.utilities.TestSuite;
import lombok.Getter;

@Getter
@TestSuite.Defaults
@SuppressWarnings("unchecked")
class SharedScraperOwnedSpecificationControllerTest implements TestSuite {
  private static final String[] TEST_GROUPS = new String[] {
      "Scoutdesk Users", "Scoutdesk Developers", "Scoutdesk Admins"
  };

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

  private static final Pair<String, Object>[] TESTS;

  // Cartesian product of TEST_GROUPS and TEST_OPTIONS
  static {
    TESTS = new Pair[TEST_GROUPS.length * TEST_OPTIONS.length];
    for (int i = 0; i < TEST_GROUPS.length; i++) {
      for (int j = 0; j < TEST_OPTIONS.length; j++) {
        final var testGroup = TEST_GROUPS[i];
        final var testOption = TEST_OPTIONS[j];
        TESTS[i * TEST_OPTIONS.length + j] = Pair.of(testGroup, testOption);
      }
    }
  }

  private final MockMvc mockMvc;
  private final ResourceUtils resourceUtils;
  private final WebscoutRestClient webscoutRestClient;
  private final PlatformTransactionManager platformTransactionManager;
  private final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository;

  SharedScraperOwnedSpecificationControllerTest(
      @Autowired final MockMvc mockMvc,
      @Autowired final ResourceUtils resourceUtils,
      @Autowired final WebscoutRestClient webscoutRestClient,
      @Autowired final PlatformTransactionManager platformTransactionManager,
      @Autowired final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository) {
    this.mockMvc = mockMvc;
    this.resourceUtils = resourceUtils;
    this.webscoutRestClient = webscoutRestClient;
    this.platformTransactionManager = platformTransactionManager;
    this.scraperOwnedSpecificationRepository = scraperOwnedSpecificationRepository;
  }

  @ParameterizedTest
  @FieldSource("TESTS")
  void givenValidUserAndParamsWhenFindSpecificationsThenReturnExpectedResults(
      final Pair<String, TestOption> test) throws Exception {
    final var testGroup = test.getLeft();
    final var testOption = test.getRight();

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
            .header("X-authentik-username", "tester-2")
            .header("X-authentik-groups", testGroup))
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
            .header("X-authentik-username", "tester-2")
            .header("X-authentik-groups", "Other"))
        .andExpect(status().isForbidden());
  }

  @Test
  void givenNoAuthWhenFindSpecificationsThenReturnUnauthorized() throws Exception {
    mockMvc
        .perform(get("/api/v1/shared/scrapers/specifications"))
        .andExpect(status().isUnauthorized());
  }

  private record TestOption(
      @Nullable String query,
      @Nullable String owner,
      @Nullable Boolean ignoreCase,
      @Nullable Boolean strictModeEnabled,
      List<Pair<String, Object>> expectedResults) {
  }
}
