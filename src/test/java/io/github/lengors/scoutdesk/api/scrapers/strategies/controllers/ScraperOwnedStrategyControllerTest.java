package io.github.lengors.scoutdesk.api.scrapers.strategies.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Collectors;

import org.awaitility.Awaitility;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;

import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileReference;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories.ScraperOwnedSpecificationRepository;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyReference;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories.ScraperOwnedStrategyRepository;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;
import io.github.lengors.scoutdesk.testing.utilities.ResourceUtils;
import io.github.lengors.scoutdesk.testing.utilities.TestSuite;

@TestSuite.Defaults
class ScraperOwnedStrategyControllerTest implements TestSuite {
  @SuppressWarnings("unused")
  private static final String[] TEST_GROUPS = new String[] {
      "Scoutdesk Users", "Scoutdesk Developers", "Scoutdesk Admins"
  };

  private final MockMvc mockMvc;
  private final ResourceUtils resourceUtils;
  private final WebscoutRestClient webscoutRestClient;
  private final PlatformTransactionManager platformTransactionManager;
  private final ScraperOwnedProfileRepository scraperOwnedProfileRepository;
  private final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository;
  private final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository;

  ScraperOwnedStrategyControllerTest(
      @Autowired final MockMvc mockMvc,
      @Autowired final ResourceUtils resourceUtils,
      @Autowired final WebscoutRestClient webscoutRestClient,
      @Autowired final PlatformTransactionManager platformTransactionManager,
      @Autowired final ScraperOwnedProfileRepository scraperOwnedProfileRepository,
      @Autowired final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository,
      @Autowired final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository) {
    this.mockMvc = mockMvc;
    this.resourceUtils = resourceUtils;
    this.webscoutRestClient = webscoutRestClient;
    this.platformTransactionManager = platformTransactionManager;
    this.scraperOwnedProfileRepository = scraperOwnedProfileRepository;
    this.scraperOwnedSpecificationRepository = scraperOwnedSpecificationRepository;
    this.scraperOwnedStrategyRepository = scraperOwnedStrategyRepository;
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
  void givenValidOwnerAndStrategyNameWhenDeleteStrategyThenStrategyIsDeleted(final String testGroup) throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/strategies/test-strategy-0")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isNoContent());

    Awaitility
        .await()
        .untilAsserted(() -> transaction(status -> {
          Assertions.assertFalse(scraperOwnedStrategyRepository
              .existsById(new ScraperOwnedStrategyReference("tester-5", "test-strategy-0")));
          Assertions.assertTrue(scraperOwnedStrategyRepository
              .existsById(new ScraperOwnedStrategyReference("tester-5", "test-strategy-1")));
          Assertions.assertTrue(scraperOwnedStrategyRepository
              .existsById(new ScraperOwnedStrategyReference("tester-6", "test-strategy-0")));
          Assertions.assertTrue(scraperOwnedStrategyRepository
              .existsById(new ScraperOwnedStrategyReference("tester-6", "test-strategy-1")));
        }));
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenIncorrectOwnerWhenDeleteStrategyThenNoStrategyIsDeleted(final String testGroup) throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/strategies/test-strategy-0")
            .header("X-authentik-username", "tester-2")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isNotFound());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenIncorrectStrategyNameWhenDeleteStrategyThenNoStrategyIsDeleted(final String testGroup) throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/strategies/test-strategy-3")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isNotFound());
  }

  @Test
  void givenNoAuthWhenDeleteStrategyThenUnauthorized() throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/strategies/test-strategy-0"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void givenForbiddenGroupWhenDeleteStrategyThenForbidden() throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/strategies/test-strategy-0")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", "Other"))
        .andExpect(status().isForbidden());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenValidOwnerWhenDeleteAllStrategiesThenStrategiesAreDeleted(final String testGroup) throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/strategies")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isNoContent());

    Awaitility
        .await()
        .untilAsserted(() -> transaction(status -> {
          Assertions.assertFalse(scraperOwnedStrategyRepository
              .existsById(new ScraperOwnedStrategyReference("tester-5", "test-strategy-0")));
          Assertions.assertFalse(scraperOwnedStrategyRepository
              .existsById(new ScraperOwnedStrategyReference("tester-5", "test-strategy-1")));
          Assertions.assertTrue(scraperOwnedStrategyRepository
              .existsById(new ScraperOwnedStrategyReference("tester-6", "test-strategy-0")));
          Assertions.assertTrue(scraperOwnedStrategyRepository
              .existsById(new ScraperOwnedStrategyReference("tester-6", "test-strategy-1")));
        }));
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenIncorrectOwnerWhenDeleteAllStrategiesThenNoStrategiesAreDeleted(final String testGroup) throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/strategies")
            .header("X-authentik-username", "tester-2")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isNoContent());

    Awaitility
        .await()
        .untilAsserted(() -> transaction(status -> {
          Assertions.assertTrue(scraperOwnedStrategyRepository
              .existsById(new ScraperOwnedStrategyReference("tester-5", "test-strategy-0")));
          Assertions.assertTrue(scraperOwnedStrategyRepository
              .existsById(new ScraperOwnedStrategyReference("tester-5", "test-strategy-1")));
          Assertions.assertTrue(scraperOwnedStrategyRepository
              .existsById(new ScraperOwnedStrategyReference("tester-6", "test-strategy-0")));
          Assertions.assertTrue(scraperOwnedStrategyRepository
              .existsById(new ScraperOwnedStrategyReference("tester-6", "test-strategy-1")));
        }));
  }

  @Test
  void givenNoAuthWhenDeleteAllStrategiesThenUnauthorized() throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/strategies"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void givenForbiddenGroupWhenDeleteAllStrategiesThenForbidden() throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/strategies")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", "Other"))
        .andExpect(status().isForbidden());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenValidOwnerAndStrategyWhenDeleteProfilesFromStrategyThenProfilesAreDeleted(final String testGroup)
      throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/strategies/test-strategy-0/profiles")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content("[\"test-profile-0\"]"))
        .andExpect(status().isNoContent());

    transaction(status -> {
      final var entity = scraperOwnedStrategyRepository
          .findById(new ScraperOwnedStrategyReference("tester-5", "test-strategy-0"));
      Assertions.assertTrue(entity.isPresent());
      final var strategy = entity.get();
      Assertions.assertFalse(strategy
          .getProfiles()
          .stream()
          .map(ScraperOwnedProfileEntity::getReference)
          .map(ScraperOwnedProfileReference::name)
          .anyMatch("test-profile-0"::equals));
      Assertions.assertFalse(strategy
          .getProfiles()
          .isEmpty());
    });
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenIncorrectOwnerWhenDeleteProfilesFromStrategyThenNoProfilesAreDeleted(final String testGroup)
      throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/strategies/test-strategy-0/profiles")
            .header("X-authentik-username", "tester-2")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content("[\"test-profile-0\"]"))
        .andExpect(status().isNotFound());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenIncorrectStrategyNameWhenDeleteProfilesFromStrategyThenNoProfilesAreDeleted(final String testGroup)
      throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/strategies/test-strategy-3/profiles")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content("[\"test-profile-0\"]"))
        .andExpect(status().isNotFound());
  }

  @Test
  void givenNoAuthWhenDeleteProfilesFromStrategyThenUnauthorized() throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/strategies/test-strategy-0/profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content("[\"test-profile-0\"]"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void givenForbiddenGroupWhenDeleteProfilesFromStrategyThenForbidden() throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/strategies/test-strategy-0/profiles")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", "Other")
            .contentType(MediaType.APPLICATION_JSON)
            .content("[\"test-profile-0\"]"))
        .andExpect(status().isForbidden());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenValidOwnerAndStrategyNameWhenFindStrategyThenStrategyIsReturned(final String testGroup) throws Exception {
    mockMvc
        .perform(get("/api/v1/scrapers/strategies/test-strategy-0")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.owner").value("tester-5"))
        .andExpect(jsonPath("$.name").value("test-strategy-0"))
        .andExpect(jsonPath("$.profiles").isArray())
        .andExpect(jsonPath("$.profiles.length()").value(2))
        .andExpect(jsonPath("$.profiles", Matchers.containsInAnyOrder("test-profile-0", "test-profile-1")));
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenIncorrectOwnerWhenFindStrategyThenNoStrategyIsReturned(final String testGroup) throws Exception {
    mockMvc
        .perform(get("/api/v1/scrapers/strategies/test-strategy-0")
            .header("X-authentik-username", "tester-2")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isNotFound());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenIncorrectStrategyNameWhenFindStrategyThenNoStrategyIsReturned(final String testGroup) throws Exception {
    mockMvc
        .perform(get("/api/v1/scrapers/strategies/test-strategy-3")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isNotFound());
  }

  @Test
  void givenNoAuthWhenFindStrategyThenUnauthorized() throws Exception {
    mockMvc
        .perform(get("/api/v1/scrapers/strategies/test-strategy-0"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void givenForbiddenGroupWhenFindStrategyThenForbidden() throws Exception {
    mockMvc
        .perform(get("/api/v1/scrapers/strategies/test-strategy-0")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", "Other"))
        .andExpect(status().isForbidden());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenValidOwnerWhenFindStrategiesThenStrategiesAreReturned(final String testGroup) throws Exception {
    mockMvc
        .perform(get("/api/v1/scrapers/strategies")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].owner").value("tester-5"))
        .andExpect(jsonPath("$[0].name").value("test-strategy-0"))
        .andExpect(jsonPath("$[0].profiles").isArray())
        .andExpect(jsonPath("$[0].profiles.length()").value(2))
        .andExpect(jsonPath("$[0].profiles[*]").value(Matchers.containsInAnyOrder("test-profile-0", "test-profile-1")))
        .andExpect(jsonPath("$[1].owner").value("tester-5"))
        .andExpect(jsonPath("$[1].name").value("test-strategy-1"))
        .andExpect(jsonPath("$[1].profiles").isArray())
        .andExpect(jsonPath("$[1].profiles.length()").value(1))
        .andExpect(jsonPath("$[1].profiles[*]").value(Matchers.containsInAnyOrder("test-profile-2")));
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenIncorrectOwnerWhenFindStrategiesThenNoStrategiesAreReturned(final String testGroup) throws Exception {
    mockMvc
        .perform(get("/api/v1/scrapers/strategies")
            .header("X-authentik-username", "tester-2")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void givenNoAuthWhenFindStrategiesThenUnauthorized() throws Exception {
    mockMvc
        .perform(get("/api/v1/scrapers/strategies"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void givenForbiddenGroupWhenFindStrategiesThenForbidden() throws Exception {
    mockMvc
        .perform(get("/api/v1/scrapers/strategies")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", "Other"))
        .andExpect(status().isForbidden());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenValidOwnerAndNewStrategyWhenSaveStrategyThenStrategyIsSaved(final String testGroup) throws Exception {
    mockMvc
        .perform(put("/api/v1/scrapers/strategies")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"test-strategy-3\",\"profiles\":[\"test-profile-0\",\"test-profile-1\"]}"))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.owner").value("tester-5"))
        .andExpect(jsonPath("$.name").value("test-strategy-3"))
        .andExpect(jsonPath("$.profiles").isArray())
        .andExpect(jsonPath("$.profiles.length()").value(2))
        .andExpect(jsonPath("$.profiles[*]").value(Matchers.containsInAnyOrder("test-profile-0", "test-profile-1")));

    transaction(status -> {
      final var entity = scraperOwnedStrategyRepository
          .findById(new ScraperOwnedStrategyReference("tester-5", "test-strategy-3"));
      Assertions.assertTrue(entity.isPresent());
      final var strategy = entity.get();
      final var reference = strategy.getReference();
      Assertions.assertEquals("tester-5", reference.owner());
      Assertions.assertEquals("test-strategy-3", reference.name());
      final var profileNames = strategy
          .getProfiles()
          .stream()
          .map(ScraperOwnedProfileEntity::getReference)
          .map(ScraperOwnedProfileReference::name)
          .collect(Collectors.toUnmodifiableSet());
      Assertions.assertEquals(2, profileNames.size());
      Assertions.assertTrue(profileNames.contains("test-profile-0"));
      Assertions.assertTrue(profileNames.contains("test-profile-1"));
    });
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenExistingStrategyReferenceWhenSaveStrategyThenConflict(final String testGroup) throws Exception {
    mockMvc
        .perform(put("/api/v1/scrapers/strategies")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"test-strategy-0\",\"profiles\":[\"test-profile-0\",\"test-profile-1\"]}"))
        .andExpect(status().isConflict());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenInvalidStrategyNameWhenSaveStrategyThenUnprocessableEntity(final String testGroup) throws Exception {
    mockMvc
        .perform(put("/api/v1/scrapers/strategies")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"test/strategy/0\",\"profiles\":[\"test-profile-0\",\"test-profile-1\"]}"))
        .andExpect(status().isUnprocessableEntity());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenMissingProfileWhenSaveStrategyThenUnprocessableEntity(final String testGroup) throws Exception {
    mockMvc
        .perform(put("/api/v1/scrapers/strategies")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"test/strategy/0\",\"profiles\":[\"test-profile-a\"]}"))
        .andExpect(status().isUnprocessableEntity());
  }

  @Test
  void givenNoAuthWhenSaveStrategyThenUnauthorized() throws Exception {
    mockMvc
        .perform(put("/api/v1/scrapers/strategies")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"test-strategy-3\",\"profiles\":[\"test-profile-0\",\"test-profile-1\"]}"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void givenForbiddenGroupWhenSaveStrategyThenForbidden() throws Exception {
    mockMvc
        .perform(put("/api/v1/scrapers/strategies")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", "Other")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"test-strategy-3\",\"profiles\":[\"test-profile-0\",\"test-profile-1\"]}"))
        .andExpect(status().isForbidden());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenValidOwnerAndStrategyWhenAddProfilesToStrategyThenProfilesAreAdded(final String testGroup)
      throws Exception {
    final var expectedCount = 3;

    mockMvc
        .perform(put("/api/v1/scrapers/strategies/test-strategy-0/profiles")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content("[\"test-profile-2\"]"))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.owner").value("tester-5"))
        .andExpect(jsonPath("$.name").value("test-strategy-0"))
        .andExpect(jsonPath("$.profiles").isArray())
        .andExpect(jsonPath("$.profiles.length()").value(expectedCount))
        .andExpect(jsonPath("$.profiles[*]")
            .value(Matchers.containsInAnyOrder("test-profile-0", "test-profile-1", "test-profile-2")));

    transaction(status -> {
      final var entity = scraperOwnedStrategyRepository
          .findById(new ScraperOwnedStrategyReference("tester-5", "test-strategy-0"));
      Assertions.assertTrue(entity.isPresent());
      final var strategy = entity.get();
      final var profileNames = strategy
          .getProfiles()
          .stream()
          .map(ScraperOwnedProfileEntity::getReference)
          .map(ScraperOwnedProfileReference::name)
          .collect(Collectors.toSet());
      Assertions.assertEquals(expectedCount, profileNames.size());
      Assertions.assertTrue(profileNames.contains("test-profile-0"));
      Assertions.assertTrue(profileNames.contains("test-profile-1"));
      Assertions.assertTrue(profileNames.contains("test-profile-2"));
    });
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenIncorrectOwnerWhenAddProfilesToStrategyThenNotFound(final String testGroup) throws Exception {
    mockMvc
        .perform(put("/api/v1/scrapers/strategies/test-strategy-0/profiles")
            .header("X-authentik-username", "tester-2")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content("[\"test-profile-2\"]"))
        .andExpect(status().isNotFound());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenIncorrectStrategyNameWhenAddProfilesToStrategyThenNotFound(final String testGroup) throws Exception {
    mockMvc
        .perform(put("/api/v1/scrapers/strategies/test-strategy-3/profiles")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content("[\"test-profile-2\"]"))
        .andExpect(status().isNotFound());
  }

  @Test
  void givenNoAuthWhenAddProfilesToStrategyThenUnauthorized() throws Exception {
    mockMvc
        .perform(put("/api/v1/scrapers/strategies/test-strategy-0/profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content("[\"test-profile-2\"]"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void givenForbiddenGroupWhenAddProfilesToStrategyThenForbidden() throws Exception {
    mockMvc
        .perform(put("/api/v1/scrapers/strategies/test-strategy-0/profiles")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", "Other")
            .contentType(MediaType.APPLICATION_JSON)
            .content("[\"test-profile-2\"]"))
        .andExpect(status().isForbidden());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenValidOwnerAndStrategyWhenUpdateStrategyThenProfilesAreUpdated(final String testGroup) throws Exception {
    mockMvc
        .perform(patch("/api/v1/scrapers/strategies/test-strategy-0")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content("[\"test-profile-2\"]"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.owner").value("tester-5"))
        .andExpect(jsonPath("$.name").value("test-strategy-0"))
        .andExpect(jsonPath("$.profiles").isArray())
        .andExpect(jsonPath("$.profiles.length()").value(1))
        .andExpect(jsonPath("$.profiles[*]").value(Matchers.containsInAnyOrder("test-profile-2")));

    transaction(status -> {
      final var entity = scraperOwnedStrategyRepository
          .findById(new ScraperOwnedStrategyReference("tester-5", "test-strategy-0"));
      Assertions.assertTrue(entity.isPresent());
      final var strategy = entity.get();
      final var reference = strategy.getReference();
      Assertions.assertEquals("tester-5", reference.owner());
      Assertions.assertEquals("test-strategy-0", reference.name());
      final var profileNames = strategy
          .getProfiles()
          .stream()
          .map(ScraperOwnedProfileEntity::getReference)
          .map(ScraperOwnedProfileReference::name)
          .collect(Collectors.toSet());
      Assertions.assertEquals(1, profileNames.size());
      Assertions.assertTrue(profileNames.contains("test-profile-2"));
      Assertions.assertFalse(profileNames.contains("test-profile-0"));
      Assertions.assertFalse(profileNames.contains("test-profile-1"));
    });
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenMissingStrategyOwnerWhenUpdateStrategyThenNotFound(final String testGroup) throws Exception {
    mockMvc
        .perform(patch("/api/v1/scrapers/strategies/test-strategy-0")
            .header("X-authentik-username", "tester-2")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content("[\"test-profile-2\"]"))
        .andExpect(status().isNotFound());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenMissingStrategyNameWhenUpdateStrategyThenNotFound(final String testGroup) throws Exception {
    mockMvc
        .perform(patch("/api/v1/scrapers/strategies/test-strategy-3")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content("[\"test-profile-2\"]"))
        .andExpect(status().isNotFound());
  }

  @Test
  void givenNoAuthWhenUpdateStrategyThenUnauthorized() throws Exception {
    mockMvc
        .perform(patch("/api/v1/scrapers/strategies/test-strategy-0")
            .contentType(MediaType.APPLICATION_JSON)
            .content("[\"test-profile-2\"]"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void givenForbiddenGroupWhenUpdateStrategyThenForbidden() throws Exception {
    mockMvc
        .perform(patch("/api/v1/scrapers/strategies/test-strategy-0")
            .header("X-authentik-username", "tester-5")
            .header("X-authentik-groups", "Other")
            .contentType(MediaType.APPLICATION_JSON)
            .content("[\"test-profile-2\"]"))
        .andExpect(status().isForbidden());
  }
}
