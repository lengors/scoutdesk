package io.github.lengors.scoutdesk.api.scrapers.specifications.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.lengors.scoutdesk.domain.io.exceptions.LoadDataFormatException;
import io.github.lengors.scoutdesk.domain.persistence.converters.EntityNotFoundExceptionReportConverter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.converters.ScraperOwnedSpecificationStatusTransitionExceptionReportConverter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationByReferenceAndStatusNotFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;
import io.github.lengors.scoutdesk.domain.spring.core.converters.MissingServletRequestPartExceptionConstraintErrorConverter;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.util.NullnessUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
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
record ScraperOwnedSpecificationRestControllerTest(
  @Autowired MockMvc mockMvc,
  @Autowired ResourceUtils resourceUtils,
  @Autowired WebscoutRestClient webscoutRestClient,
  @Autowired PlatformTransactionManager platformTransactionManager,
  @Autowired ScraperOwnedProfileRepository scraperOwnedProfileRepository,
  @Autowired ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository,
  @Autowired ScraperOwnedStrategyRepository scraperOwnedStrategyRepository
) implements TestSuite {
  @Test
  void givenValidSpecificationAndOwnerWhenDeleteSpecificationThenSpecificationIsDeleted() throws Exception {
    mockMvc
      .perform(delete("/api/v1/scrapers/specifications/test-specification-2")
        .header("X-authentik-username", "tester-0"))
      .andExpect(status().isNoContent());

    transaction(status -> Assertions.assertFalse(scraperOwnedSpecificationRepository
      .existsById(new ScraperOwnedSpecificationReference("tester-0", "test-specification-2"))));
  }

  @Test
  void givenSpecificationUsedByProfileWhenDeleteSpecificationThenStatusIsDeleted() throws Exception {
    mockMvc
      .perform(delete("/api/v1/scrapers/specifications/test-specification-0")
        .header("X-authentik-username", "tester-0"))
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

  @Test
  void givenAlreadyDeletedSpecificationWhenDeleteSpecificationThenReturnNotFound() throws Exception {
    mockMvc
      .perform(delete("/api/v1/scrapers/specifications/test-specification-1")
        .header("X-authentik-username", "tester-0"))
      .andExpect(status().isNotFound())
      .andExpect(content().string(
        EntityNotFoundExceptionReportConverter.MESSAGE.formatted(
          NullnessUtil
            .castNonNull(ScraperOwnedSpecificationEntity.class)
            .getSimpleName(),
          new ScraperOwnedSpecificationByReferenceAndStatusNotFilter(
            new ScraperOwnedSpecificationReference("tester-0", "test-specification-1")))));
  }

  @Test
  void givenIncorrectOwnerWhenDeleteSpecificationThenReturnNotFound() throws Exception {
    mockMvc
      .perform(delete("/api/v1/scrapers/specifications/test-specification-0")
        .header("X-authentik-username", "tester-2"))
      .andExpect(status().isNotFound())
      .andExpect(content().string(
        EntityNotFoundExceptionReportConverter.MESSAGE.formatted(
          NullnessUtil
            .castNonNull(ScraperOwnedSpecificationEntity.class)
            .getSimpleName(),
          new ScraperOwnedSpecificationByReferenceAndStatusNotFilter(
            new ScraperOwnedSpecificationReference("tester-2", "test-specification-0")))));
  }

  @Test
  void givenIncorrectNameWhenDeleteSpecificationThenReturnNotFound() throws Exception {
    mockMvc
      .perform(delete("/api/v1/scrapers/specifications/test-specification-3")
        .header("X-authentik-username", "tester-0"))
      .andExpect(status().isNotFound())
      .andExpect(content().string(
        EntityNotFoundExceptionReportConverter.MESSAGE.formatted(
          NullnessUtil
            .castNonNull(ScraperOwnedSpecificationEntity.class)
            .getSimpleName(),
          new ScraperOwnedSpecificationByReferenceAndStatusNotFilter(
            new ScraperOwnedSpecificationReference("tester-0", "test-specification-3")))));
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
        .header("X-authentik-username", "tester-9"))
      .andExpect(status().isForbidden());
  }

  @Test
  void givenValidUserWhenDeleteAllSpecificationsThenCorrectSpecificationsRemain() throws Exception {
    mockMvc
      .perform(delete("/api/v1/scrapers/specifications")
        .header("X-authentik-username", "tester-0"))
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

  @Test
  void givenUserWithNoOwnedSpecificationsWhenDeleteAllSpecificationsThenNoChange() throws Exception {
    mockMvc
      .perform(delete("/api/v1/scrapers/specifications")
        .header("X-authentik-username", "tester-2"))
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
        .header("X-authentik-username", "tester-9"))
      .andExpect(status().isForbidden());
  }

  @Test
  void givenValidUserAndExistingSpecificationWhenFindSpecificationThenReturnSpecification() throws Exception {
    mockMvc
      .perform(get("/api/v1/scrapers/specifications/test-specification-0")
        .header("X-authentik-username", "tester-0"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.owner").value("tester-0"))
      .andExpect(jsonPath("$.specification.name").value("test-specification-0"))
      .andExpect(jsonPath("$.status").value("active"));
  }

  @Test
  void givenIncorrectOwnerWhenFindSpecificationThenReturnNotFound() throws Exception {
    mockMvc
      .perform(get("/api/v1/scrapers/specifications/test-specification-0")
        .header("X-authentik-username", "tester-2"))
      .andExpect(status().isNotFound())
      .andExpect(content().string(
        EntityNotFoundExceptionReportConverter.MESSAGE.formatted(
          NullnessUtil
            .castNonNull(ScraperOwnedSpecificationEntity.class)
            .getSimpleName(),
          new ScraperOwnedSpecificationByReferenceAndStatusNotFilter(
            new ScraperOwnedSpecificationReference("tester-2", "test-specification-0")))));
  }

  @Test
  void givenIncorrectNameWhenFindSpecificationThenReturnNotFound() throws Exception {
    mockMvc
      .perform(get("/api/v1/scrapers/specifications/test-specification-3")
        .header("X-authentik-username", "tester-0"))
      .andExpect(status().isNotFound())
      .andExpect(content().string(
        EntityNotFoundExceptionReportConverter.MESSAGE.formatted(
          NullnessUtil
            .castNonNull(ScraperOwnedSpecificationEntity.class)
            .getSimpleName(),
          new ScraperOwnedSpecificationByReferenceAndStatusNotFilter(
            new ScraperOwnedSpecificationReference("tester-0", "test-specification-3")))));
  }

  @Test
  void givenDeletedSpecificationWhenFindSpecificationThenReturnNotFound() throws Exception {
    mockMvc
      .perform(get("/api/v1/scrapers/specifications/test-specification-1")
        .header("X-authentik-username", "tester-0"))
      .andExpect(status().isNotFound())
      .andExpect(content().string(
        EntityNotFoundExceptionReportConverter.MESSAGE.formatted(
          NullnessUtil
            .castNonNull(ScraperOwnedSpecificationEntity.class)
            .getSimpleName(),
          new ScraperOwnedSpecificationByReferenceAndStatusNotFilter(
            new ScraperOwnedSpecificationReference("tester-0", "test-specification-1")))));
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
        .header("X-authentik-username", "tester-9"))
      .andExpect(status().isForbidden());
  }

  @Test
  void givenValidUserWhenFindAllSpecificationsThenReturnSpecifications() throws Exception {
    mockMvc
      .perform(get("/api/v1/scrapers/specifications")
        .header("X-authentik-username", "tester-0"))
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

  @Test
  void givenUserWithNoOwnedSpecificationsWhenFindAllSpecificationsThenReturnEmpty() throws Exception {
    mockMvc
      .perform(get("/api/v1/scrapers/specifications")
        .header("X-authentik-username", "tester-2"))
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
        .header("X-authentik-username", "tester-9"))
      .andExpect(status().isForbidden());
  }

  @Test
  void givenValidUserAndValidSpecificationWhenSaveSpecificationThenSpecificationIsSaved() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"name\":\"test-specification-2\",\"settings\":{\"defaults\":{\"url\":{\"location\":\"http://test\"},\"gates\":[]},\"locale\":\"en-GB\",\"timezone\":\"UTC\"},\"handlers\":[]}";

    mockMvc
      .perform(put("/api/v1/scrapers/specifications")
        .header("X-authentik-username", "tester-1")
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

  @Test
  void givenExistingSpecificationReferenceWhenSaveSpecificationThenReturnConflict() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"name\":\"test-specification-2\",\"settings\":{\"defaults\":{\"url\":{\"location\":\"http://test\"},\"gates\":[]},\"locale\":\"en-GB\",\"timezone\":\"UTC\"},\"handlers\":[]}";

    mockMvc
      .perform(put("/api/v1/scrapers/specifications")
        .header("X-authentik-username", "tester-0")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.length()").value(1))
      .andExpect(jsonPath("$[0].property").value("name"))
      .andExpect(jsonPath("$[0].message").value("specification already exists"));
  }

  @Test
  void givenInvalidSpecificationNameWhenSaveSpecificationThenReturnUnprocessableEntity() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"name\":\"test/specification/2\",\"settings\":{\"defaults\":{\"url\":{\"location\":\"http://test\"},\"gates\":[]},\"locale\":\"en-GB\",\"timezone\":\"UTC\"},\"handlers\":[]}";

    mockMvc
      .perform(put("/api/v1/scrapers/specifications")
        .header("X-authentik-username", "tester-0")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isUnprocessableEntity());
  }

  @Test
  void givenNoAuthWhenSaveSpecificationThenReturnUnauthorized() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"name\":\"test-specification-2\",\"settings\":{\"defaults\":{\"url\":{\"location\":\"http://test\"},\"gates\":[]},\"locale\":\"en-GB\",\"timezone\":\"UTC\"},\"handlers\":[]}";

    mockMvc
      .perform(put("/api/v1/scrapers/specifications")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isUnauthorized());
  }

  @Test
  void givenUserWithForbiddenGroupWhenSaveSpecificationThenReturnForbidden() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"name\":\"test-specification-2\",\"settings\":{\"defaults\":{\"url\":{\"location\":\"http://test\"},\"gates\":[]},\"locale\":\"en-GB\",\"timezone\":\"UTC\"},\"handlers\":[]}";

    mockMvc
      .perform(put("/api/v1/scrapers/specifications")
        .header("X-authentik-username", "tester-9")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isForbidden());
  }

  @Test
  void givenValidUserAndSpecificationWhenUpdateStatusToActiveThenStatusIsActive() throws Exception {
    mockMvc
      .perform(patch("/api/v1/scrapers/specifications/test-specification-2")
        .header("X-authentik-username", "tester-0")
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

  @Test
  void givenValidUserAndSpecificationWhenUpdateStatusToArchivedThenStatusIsArchived() throws Exception {
    mockMvc
      .perform(patch("/api/v1/scrapers/specifications/test-specification-0")
        .header("X-authentik-username", "tester-0")
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

  @Test
  void givenInvalidStatusTransitionWhenUpdateStatusToActiveThenReturnUnprocessableEntity() throws Exception {
    mockMvc
      .perform(patch("/api/v1/scrapers/specifications/test-specification-0")
        .header("X-authentik-username", "tester-0")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"action\":\"activate\"}"))
      .andExpect(status().isUnprocessableEntity())
      .andExpect(content().string(
        ScraperOwnedSpecificationStatusTransitionExceptionReportConverter.MESSAGE.formatted(
          ScraperOwnedSpecificationStatus.ACTIVE, ScraperOwnedSpecificationStatus.ACTIVE)));
  }

  @Test
  void givenInvalidStatusTransitionWhenUpdateStatusToArchivedThenReturnUnprocessableEntity() throws Exception {
    mockMvc
      .perform(patch("/api/v1/scrapers/specifications/test-specification-2")
        .header("X-authentik-username", "tester-0")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"action\":\"archive\"}"))
      .andExpect(status().isUnprocessableEntity())
      .andExpect(content().string(
        ScraperOwnedSpecificationStatusTransitionExceptionReportConverter.MESSAGE.formatted(
          ScraperOwnedSpecificationStatus.ARCHIVED, ScraperOwnedSpecificationStatus.ARCHIVED)));
  }

  @Test
  void givenDeletedSpecificationWhenUpdateStatusThenReturnNotFound() throws Exception {
    mockMvc
      .perform(patch("/api/v1/scrapers/specifications/test-specification-1")
        .header("X-authentik-username", "tester-0")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"action\":\"archive\"}"))
      .andExpect(status().isNotFound())
      .andExpect(content().string(
        EntityNotFoundExceptionReportConverter.MESSAGE.formatted(
          NullnessUtil
            .castNonNull(ScraperOwnedSpecificationEntity.class)
            .getSimpleName(),
          new ScraperOwnedSpecificationByReferenceAndStatusNotFilter(
            new ScraperOwnedSpecificationReference("tester-0", "test-specification-1")))));
  }

  @Test
  void givenMissingSpecificationOwnerWhenUpdateStatusThenReturnNotFound() throws Exception {
    mockMvc
      .perform(patch("/api/v1/scrapers/specifications/test-specification-0")
        .header("X-authentik-username", "tester-2")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"action\":\"archive\"}"))
      .andExpect(status().isNotFound())
      .andExpect(content().string(
        EntityNotFoundExceptionReportConverter.MESSAGE.formatted(
          NullnessUtil
            .castNonNull(ScraperOwnedSpecificationEntity.class)
            .getSimpleName(),
          new ScraperOwnedSpecificationByReferenceAndStatusNotFilter(
            new ScraperOwnedSpecificationReference("tester-2", "test-specification-0")))));
  }

  @Test
  void givenMissingSpecificationNameWhenUpdateStatusThenReturnNotFound() throws Exception {
    mockMvc
      .perform(patch("/api/v1/scrapers/specifications/test-specification-2")
        .header("X-authentik-username", "tester-1")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"action\":\"archive\"}"))
      .andExpect(status().isNotFound())
      .andExpect(content().string(
        EntityNotFoundExceptionReportConverter.MESSAGE.formatted(
          NullnessUtil
            .castNonNull(ScraperOwnedSpecificationEntity.class)
            .getSimpleName(),
          new ScraperOwnedSpecificationByReferenceAndStatusNotFilter(
            new ScraperOwnedSpecificationReference("tester-1", "test-specification-2")))));
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
        .header("X-authentik-username", "tester-9")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"action\":\"archive\"}"))
      .andExpect(status().isForbidden());
  }

  @Test
  void givenValidUserAndValidSpecificationWhenUploadSpecificationThenSpecificationIsSaved() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "name: test-specification-2\nsettings:\n  defaults:\n    url:\n      location: http://test\n    gates: []\n  locale: en-GB\n  timezone: UTC\nhandlers: []\n";
    final var file = new MockMultipartFile(
      "specification",
      "specification.yaml",
      MediaType.APPLICATION_YAML_VALUE,
      content.getBytes());

    mockMvc
      .perform(multipart("/api/v1/scrapers/specifications")
        .file(file)
        .header("X-authentik-username", "tester-1")
        .with(request -> {
          request.setMethod(HttpMethod.PUT.name());
          return request;
        }))
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

  @Test
  void givenExistingSpecificationReferenceWhenUploadSpecificationThenReturnConflict() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "name: test-specification-2\nsettings:\n  defaults:\n    url:\n      location: http://test\n    gates: []\n  locale: en-GB\n  timezone: UTC\nhandlers: []\n";
    final var file = new MockMultipartFile(
      "specification",
      "specification.yaml",
      MediaType.APPLICATION_YAML_VALUE,
      content.getBytes());

    mockMvc
      .perform(multipart("/api/v1/scrapers/specifications")
        .file(file)
        .header("X-authentik-username", "tester-0")
        .with(request -> {
          request.setMethod(HttpMethod.PUT.name());
          return request;
        }))
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.length()").value(1))
      .andExpect(jsonPath("$[0].property").value("specification.name"))
      .andExpect(jsonPath("$[0].message").value("specification already exists"));
  }

  @Test
  void givenInvalidSpecificationFormatWhenUploadSpecificationThenReturnUnprocessableEntity() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "name: test-specification-2\nsettings:\n  defaults:\n    url:\n      location: http://test\n    gates: {]\n  locale: en-GB\n  timezone: UTC\nhandlers: []\n";
    final var file = new MockMultipartFile(
      "specification",
      "specification.yaml",
      MediaType.APPLICATION_YAML_VALUE,
      content.getBytes());

    mockMvc
      .perform(multipart("/api/v1/scrapers/specifications")
        .file(file)
        .header("X-authentik-username", "tester-1")
        .with(request -> {
          request.setMethod(HttpMethod.PUT.name());
          return request;
        }))
      .andExpect(status().isUnprocessableEntity())
      .andExpect(jsonPath("$.length()").value(1))
      .andExpect(jsonPath("$[0].property").value("specification"))
      .andExpect(jsonPath("$[0].message").value(LoadDataFormatException.MESSAGE));
  }

  @Test
  void givenInvalidSpecificationNameWhenUploadSpecificationThenReturnUnprocessableEntity() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "name: test/specification/2\nsettings:\n  defaults:\n    url:\n      location: http://test\n    gates: []\n  locale: en-GB\n  timezone: UTC\nhandlers: []\n";
    final var file = new MockMultipartFile(
      "specification",
      "specification.yaml",
      MediaType.APPLICATION_YAML_VALUE,
      content.getBytes());

    mockMvc
      .perform(multipart("/api/v1/scrapers/specifications")
        .file(file)
        .header("X-authentik-username", "tester-0")
        .with(request -> {
          request.setMethod(HttpMethod.PUT.name());
          return request;
        }))
      .andExpect(status().isUnprocessableEntity());
  }

  @Test
  void givenInvalidSpecificationSettingsGatesWhenUploadSpecificationThenReturnUnprocessableEntity() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "name: test-specification-2\nsettings:\n  locale: en-GB\n  timezone: UTC\nhandlers: []\n";
    final var file = new MockMultipartFile(
      "specification",
      "specification.yaml",
      MediaType.APPLICATION_YAML_VALUE,
      content.getBytes());

    mockMvc
      .perform(multipart("/api/v1/scrapers/specifications")
        .file(file)
        .header("X-authentik-username", "tester-1")
        .with(request -> {
          request.setMethod(HttpMethod.PUT.name());
          return request;
        }))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.length()").value(1))
      .andExpect(jsonPath("$[0].property").value("specification.settings.defaults"))
      .andExpect(jsonPath("$[0].message").value("must not be null"))
      .andExpect(jsonPath("$[0].category").value(NotNull.class.getCanonicalName()));
  }

  @Test
  void givenNoAuthWhenUploadSpecificationThenReturnUnauthorized() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "name: test-specification-2\nsettings:\n  defaults:\n    url:\n      location: http://test\n    gates: []\n  locale: en-GB\n  timezone: UTC\nhandlers: []\n";
    final var file = new MockMultipartFile(
      "specification",
      "specification.yaml",
      MediaType.APPLICATION_YAML_VALUE,
      content.getBytes());

    mockMvc
      .perform(multipart("/api/v1/scrapers/specifications")
        .file(file)
        .with(request -> {
          request.setMethod(HttpMethod.PUT.name());
          return request;
        }))
      .andExpect(status().isUnauthorized());
  }

  @Test
  void givenUserWithForbiddenGroupWhenUploadSpecificationThenReturnForbidden() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "name: test-specification-2\nsettings:\n  defaults:\n    url:\n      location: http://test\n    gates: []\n  locale: en-GB\n  timezone: UTC\nhandlers: []\n";
    final var file = new MockMultipartFile(
      "specification",
      "specification.yaml",
      MediaType.APPLICATION_YAML_VALUE,
      content.getBytes());

    mockMvc
      .perform(multipart("/api/v1/scrapers/specifications")
        .file(file)
        .header("X-authentik-username", "tester-9")
        .with(request -> {
          request.setMethod(HttpMethod.PUT.name());
          return request;
        }))
      .andExpect(status().isForbidden());
  }

  @Test
  void givenMissingSpecificationFieldRequestWhenUploadSpecificationThenReturnBadRequest() throws Exception {
    mockMvc
      .perform(multipart("/api/v1/scrapers/specifications")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
        .header("X-authentik-username", "tester-1")
        .with(request -> {
          request.setMethod(HttpMethod.PUT.name());
          return request;
        }))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.length()").value(1))
      .andExpect(jsonPath("$[0].property").value("specification"))
      .andExpect(jsonPath("$[0].message").value(
        MissingServletRequestPartExceptionConstraintErrorConverter.MESSAGE.formatted("specification")));
  }
}
