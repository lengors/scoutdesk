package io.github.lengors.scoutdesk.api.scrapers.specifications.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;

import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories.ScraperOwnedSpecificationRepository;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories.ScraperOwnedStrategyRepository;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;
import io.github.lengors.scoutdesk.testing.utilities.ResourceUtils;
import io.github.lengors.scoutdesk.testing.utilities.TestSuite;

@TestSuite.Defaults
class ScraperOwnedSpecificationControllerTest implements TestSuite {

  @SuppressWarnings("unused")
  private static final String[] TEST_GROUPS = new String[] { "Scoutdesk Developers", "Scoutdesk Admins" };

  private final MockMvc mockMvc;
  private final ResourceUtils resourceUtils;
  private final WebscoutRestClient webscoutRestClient;
  private final PlatformTransactionManager platformTransactionManager;
  private final ScraperOwnedProfileRepository scraperOwnedProfileRepository;
  private final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository;
  private final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository;

  ScraperOwnedSpecificationControllerTest(
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
  void givenValidSpecificationAndOwnerWhenDeleteSpecificationThenSpecificationIsDeleted(final String testGroup)
      throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/specifications/test-specification-2")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isNoContent());

    transaction(status -> Assertions.assertFalse(scraperOwnedSpecificationRepository
        .existsById(new ScraperOwnedSpecificationReference("tester-0", "test-specification-2"))));
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenSpecificationUsedByProfileWhenDeleteSpecificationThenStatusIsDeleted(final String testGroup)
      throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/specifications/test-specification-0")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isNoContent());

    transaction(status -> {
      final var entity = scraperOwnedSpecificationRepository
          .findById(new ScraperOwnedSpecificationReference("tester-0", "test-specification-0"));
      Assertions.assertTrue(entity.isPresent());
      entity.ifPresent(specificationEntity -> {
        final var reference = specificationEntity.getReference();
        Assertions.assertEquals("tester-0", reference.owner());
        Assertions.assertEquals("test-specification-0", reference.name());
        Assertions.assertEquals(ScraperOwnedSpecificationStatus.DELETED, specificationEntity.getStatus());
      });
    });
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenAlreadyDeletedSpecificationWhenDeleteSpecificationThenReturnNotFound(final String testGroup)
      throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/specifications/test-specification-1")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isNotFound());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenIncorrectOwnerWhenDeleteSpecificationThenReturnNotFound(final String testGroup) throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/specifications/test-specification-0")
            .header("X-authentik-username", "tester-2")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isNotFound());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenIncorrectNameWhenDeleteSpecificationThenReturnNotFound(final String testGroup)
      throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/specifications/test-specification-3")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isNotFound());
  }

  @Test
  void givenNoAuthWhenDeleteSpecificationThenReturnUnauthorized() throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/specifications/test-specification-0"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void givenUserWithForbiddenGroupWhenDeleteSpecificationThenReturnForbidden() throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/specifications/test-specification-0")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", "Scoutdesk Users"))
        .andExpect(status().isForbidden());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenValidUserWhenDeleteAllSpecificationsThenCorrectSpecificationsRemain(final String testGroup)
      throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/specifications")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isNoContent());

    transaction(status -> {
      Assertions.assertEquals(2, scraperOwnedSpecificationRepository
          .findAllByReferenceOwner("tester-0")
          .size());
      final var entity = scraperOwnedSpecificationRepository
          .findById(new ScraperOwnedSpecificationReference("tester-0", "test-specification-0"));
      Assertions.assertTrue(entity.isPresent());
      entity.ifPresent(specificationEntity -> {
        final var reference = specificationEntity.getReference();
        Assertions.assertEquals("tester-0", reference.owner());
        Assertions.assertEquals("test-specification-0", reference.name());
        Assertions.assertEquals(ScraperOwnedSpecificationStatus.DELETED, specificationEntity.getStatus());
      });
      Assertions.assertTrue(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-0", "test-specification-1")));
      Assertions.assertFalse(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-0", "test-specification-2")));
      Assertions.assertTrue(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-1", "test-specification-0")));
      Assertions.assertTrue(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-1", "test-specification-1")));
    });
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenUserWithNoOwnedSpecificationsWhenDeleteAllSpecificationsThenNoChange(final String testGroup)
      throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/specifications")
            .header("X-authentik-username", "tester-2")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isNoContent());

    transaction(status -> {
      Assertions.assertTrue(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-0", "test-specification-0")));
      Assertions.assertTrue(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-0", "test-specification-1")));
      Assertions.assertTrue(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-0", "test-specification-2")));
      Assertions.assertTrue(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-1", "test-specification-0")));
      Assertions.assertTrue(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-1", "test-specification-1")));
    });
  }

  @Test
  void givenNoAuthWhenDeleteAllSpecificationsThenReturnUnauthorized() throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/specifications"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void givenUserWithForbiddenGroupWhenDeleteAllSpecificationsThenReturnForbidden() throws Exception {
    mockMvc
        .perform(delete("/api/v1/scrapers/specifications")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", "Scoutdesk Users"))
        .andExpect(status().isForbidden());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenValidUserAndExistingSpecificationWhenFindSpecificationThenReturnSpecification(final String testGroup)
      throws Exception {
    mockMvc
        .perform(get("/api/v1/scrapers/specifications/test-specification-0")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.owner").value("tester-0"))
        .andExpect(jsonPath("$.specification.name").value("test-specification-0"))
        .andExpect(jsonPath("$.status").value("active"));
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenIncorrectOwnerWhenFindSpecificationThenReturnNotFound(final String testGroup) throws Exception {
    mockMvc
        .perform(get("/api/v1/scrapers/specifications/test-specification-0")
            .header("X-authentik-username", "tester-2")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isNotFound());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenIncorrectNameWhenFindSpecificationThenReturnNotFound(final String testGroup) throws Exception {
    mockMvc
        .perform(get("/api/v1/scrapers/specifications/test-specification-3")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isNotFound());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenDeletedSpecificationWhenFindSpecificationThenReturnNotFound(final String testGroup) throws Exception {
    mockMvc
        .perform(get("/api/v1/scrapers/specifications/test-specification-1")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isNotFound());
  }

  @Test
  void givenNoAuthWhenFindSpecificationThenReturnUnauthorized() throws Exception {
    mockMvc
        .perform(get("/api/v1/scrapers/specifications/test-specification-0"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void givenUserWithForbiddenGroupWhenFindSpecificationThenReturnForbidden() throws Exception {
    mockMvc
        .perform(get("/api/v1/scrapers/specifications/test-specification-0")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", "Scoutdesk Users"))
        .andExpect(status().isForbidden());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenValidUserWhenFindAllSpecificationsThenReturnSpecifications(final String testGroup) throws Exception {
    mockMvc
        .perform(get("/api/v1/scrapers/specifications")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].owner").value("tester-0"))
        .andExpect(jsonPath("$[0].specification.name").value("test-specification-0"))
        .andExpect(jsonPath("$[0].status").value("active"))
        .andExpect(jsonPath("$[1].owner").value("tester-0"))
        .andExpect(jsonPath("$[1].specification.name").value("test-specification-2"))
        .andExpect(jsonPath("$[1].status").value("archived"));
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenUserWithNoOwnedSpecificationsWhenFindAllSpecificationsThenReturnEmpty(final String testGroup)
      throws Exception {
    mockMvc
        .perform(get("/api/v1/scrapers/specifications")
            .header("X-authentik-username", "tester-2")
            .header("X-authentik-groups", testGroup))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void givenNoAuthWhenFindAllSpecificationsThenReturnUnauthorized() throws Exception {
    mockMvc
        .perform(get("/api/v1/scrapers/specifications"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void givenUserWithForbiddenGroupWhenFindAllSpecificationsThenReturnForbidden() throws Exception {
    mockMvc
        .perform(get("/api/v1/scrapers/specifications")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", "Scoutdesk Users"))
        .andExpect(status().isForbidden());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenValidUserAndValidSpecificationWhenSaveSpecificationThenSpecificationIsSaved(final String testGroup)
      throws Exception {
    @SuppressWarnings("LineLength")
    final var content = "{\"name\":\"test-specification-2\",\"settings\":{\"defaults\":{\"url\":{\"location\":\"http://test\"},\"gates\":[]},\"locale\":\"en-GB\",\"timezone\":\"UTC\"},\"handlers\":[]}";

    mockMvc
        .perform(put("/api/v1/scrapers/specifications")
            .header("X-authentik-username", "tester-1")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.owner").value("tester-1"))
        .andExpect(jsonPath("$.specification.name").value("test-specification-2"))
        .andExpect(jsonPath("$.specification.settings.defaults.url.location.jexl").value("'http://test'"))
        .andExpect(jsonPath("$.specification.settings.defaults.gates").isArray())
        .andExpect(jsonPath("$.specification.settings.defaults.gates").isEmpty())
        .andExpect(jsonPath("$.specification.settings.locale.jexl").value("'en-GB'"))
        .andExpect(jsonPath("$.specification.settings.timezone.jexl").value("'UTC'"))
        .andExpect(jsonPath("$.specification.handlers").isArray())
        .andExpect(jsonPath("$.specification.handlers").isEmpty())
        .andExpect(jsonPath("$.status").value("active"));

    transaction(status -> {
      final var expectedCount = 3;
      Assertions.assertEquals(expectedCount, scraperOwnedSpecificationRepository
          .findAllByReferenceOwner("tester-1")
          .size());
      final var entity = scraperOwnedSpecificationRepository
          .findById(new ScraperOwnedSpecificationReference("tester-1", "test-specification-2"));
      Assertions.assertTrue(entity.isPresent());
      entity.ifPresent(specificationEntity -> {
        final var reference = specificationEntity.getReference();
        Assertions.assertEquals("tester-1", reference.owner());
        Assertions.assertEquals("test-specification-2", reference.name());
        Assertions.assertEquals(ScraperOwnedSpecificationStatus.ACTIVE, specificationEntity.getStatus());
        Assertions.assertTrue(specificationEntity
            .getProfiles()
            .isEmpty());
      });
    });
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenExistingSpecificationReferenceWhenSaveSpecificationThenReturnConflict(final String testGroup)
      throws Exception {
    @SuppressWarnings("LineLength")
    final var content = "{\"name\":\"test-specification-2\",\"settings\":{\"defaults\":{\"url\":{\"location\":\"http://test\"},\"gates\":[]},\"locale\":\"en-GB\",\"timezone\":\"UTC\"},\"handlers\":[]}";

    mockMvc
        .perform(put("/api/v1/scrapers/specifications")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
        .andExpect(status().isConflict());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenInvalidSpecificationNameWhenSaveSpecificationThenReturnUnprocessableEntity(final String testGroup)
      throws Exception {
    @SuppressWarnings("LineLength")
    final var content = "{\"name\":\"test/specification/2\",\"settings\":{\"defaults\":{\"url\":{\"location\":\"http://test\"},\"gates\":[]},\"locale\":\"en-GB\",\"timezone\":\"UTC\"},\"handlers\":[]}";

    mockMvc
        .perform(put("/api/v1/scrapers/specifications")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
        .andExpect(status().isUnprocessableEntity());
  }

  @Test
  void givenNoAuthWhenSaveSpecificationThenReturnUnauthorized() throws Exception {
    @SuppressWarnings("LineLength")
    final var content = "{\"name\":\"test-specification-2\",\"settings\":{\"defaults\":{\"url\":{\"location\":\"http://test\"},\"gates\":[]},\"locale\":\"en-GB\",\"timezone\":\"UTC\"},\"handlers\":[]}";

    mockMvc
        .perform(put("/api/v1/scrapers/specifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void givenUserWithForbiddenGroupWhenSaveSpecificationThenReturnForbidden() throws Exception {
    @SuppressWarnings("LineLength")
    final var content = "{\"name\":\"test-specification-2\",\"settings\":{\"defaults\":{\"url\":{\"location\":\"http://test\"},\"gates\":[]},\"locale\":\"en-GB\",\"timezone\":\"UTC\"},\"handlers\":[]}";

    mockMvc
        .perform(put("/api/v1/scrapers/specifications")
            .header("X-authentik-username", "tester-1")
            .header("X-authentik-groups", "Scoutdesk Users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
        .andExpect(status().isForbidden());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenValidUserAndSpecificationWhenUpdateStatusToActiveThenStatusIsActive(final String testGroup)
      throws Exception {
    mockMvc
        .perform(patch("/api/v1/scrapers/specifications/test-specification-2")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"action\":\"activate\"}"))
        .andExpect(status().isOk());

    transaction(status -> {
      final var entity = scraperOwnedSpecificationRepository
          .findById(new ScraperOwnedSpecificationReference("tester-0", "test-specification-2"));
      Assertions.assertTrue(entity.isPresent());
      entity.ifPresent(specificationEntity -> {
        final var reference = specificationEntity.getReference();
        Assertions.assertEquals("tester-0", reference.owner());
        Assertions.assertEquals("test-specification-2", reference.name());
        Assertions.assertEquals(ScraperOwnedSpecificationStatus.ACTIVE, specificationEntity.getStatus());
      });
    });
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenValidUserAndSpecificationWhenUpdateStatusToArchivedThenStatusIsArchived(final String testGroup)
      throws Exception {
    mockMvc
        .perform(patch("/api/v1/scrapers/specifications/test-specification-0")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"action\":\"archive\"}"))
        .andExpect(status().isOk());

    transaction(status -> {
      final var entity = scraperOwnedSpecificationRepository
          .findById(new ScraperOwnedSpecificationReference("tester-0", "test-specification-0"));
      Assertions.assertTrue(entity.isPresent());
      entity.ifPresent(specificationEntity -> {
        final var reference = specificationEntity.getReference();
        Assertions.assertEquals("tester-0", reference.owner());
        Assertions.assertEquals("test-specification-0", reference.name());
        Assertions.assertEquals(ScraperOwnedSpecificationStatus.ARCHIVED, specificationEntity.getStatus());
      });
    });
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenInvalidStatusTransitionWhenUpdateStatusToActiveThenReturnUnprocessableEntity(final String testGroup)
      throws Exception {
    mockMvc
        .perform(patch("/api/v1/scrapers/specifications/test-specification-0")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"action\":\"activate\"}"))
        .andExpect(status().isUnprocessableEntity());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenInvalidStatusTransitionWhenUpdateStatusToArchivedThenReturnUnprocessableEntity(final String testGroup)
      throws Exception {
    mockMvc
        .perform(patch("/api/v1/scrapers/specifications/test-specification-2")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"action\":\"archive\"}"))
        .andExpect(status().isUnprocessableEntity());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenDeletedSpecificationWhenUpdateStatusThenReturnNotFound(final String testGroup) throws Exception {
    mockMvc
        .perform(patch("/api/v1/scrapers/specifications/test-specification-1")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"action\":\"archive\"}"))
        .andExpect(status().isNotFound());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenMissingSpecificationOwnerWhenUpdateStatusThenReturnNotFound(final String testGroup) throws Exception {
    mockMvc
        .perform(patch("/api/v1/scrapers/specifications/test-specification-0")
            .header("X-authentik-username", "tester-2")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"action\":\"archive\"}"))
        .andExpect(status().isNotFound());
  }

  @ParameterizedTest
  @FieldSource("TEST_GROUPS")
  void givenMissingSpecificationNameWhenUpdateStatusThenReturnNotFound(final String testGroup) throws Exception {
    mockMvc
        .perform(patch("/api/v1/scrapers/specifications/test-specification-2")
            .header("X-authentik-username", "tester-1")
            .header("X-authentik-groups", testGroup)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"action\":\"archive\"}"))
        .andExpect(status().isNotFound());
  }

  @Test
  void givenNoAuthWhenUpdateStatusThenReturnUnauthorized() throws Exception {
    mockMvc
        .perform(patch("/api/v1/scrapers/specifications/test-specification-0")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"action\":\"archive\"}"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void givenUserWithForbiddenGroupWhenUpdateStatusThenReturnForbidden() throws Exception {
    mockMvc
        .perform(patch("/api/v1/scrapers/specifications/test-specification-0")
            .header("X-authentik-username", "tester-0")
            .header("X-authentik-groups", "Scoutdesk Users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"action\":\"archive\"}"))
        .andExpect(status().isForbidden());
  }
}
