package io.github.lengors.scoutdesk.api.scrapers.profiles.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.lengors.scoutdesk.domain.persistence.converters.EntityConflictExceptionReportConverter;
import io.github.lengors.scoutdesk.domain.persistence.converters.EntityNotFoundExceptionReportConverter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchByReferenceOwnerFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileByReferrerFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import org.awaitility.Awaitility;
import org.checkerframework.checker.nullness.util.NullnessUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;

import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileReference;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories.ScraperOwnedSpecificationRepository;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories.ScraperOwnedStrategyRepository;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;
import io.github.lengors.scoutdesk.testing.utilities.ResourceUtils;
import io.github.lengors.scoutdesk.testing.utilities.TestSuite;

@TestSuite.Defaults
record ScraperOwnedProfileRestControllerTest(
  @Autowired MockMvc mockMvc,
  @Autowired ResourceUtils resourceUtils,
  @Autowired WebscoutRestClient webscoutRestClient,
  @Autowired PlatformTransactionManager platformTransactionManager,
  @Autowired ScraperOwnedProfileRepository scraperOwnedProfileRepository,
  @Autowired ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository,
  @Autowired ScraperOwnedStrategyRepository scraperOwnedStrategyRepository
) implements TestSuite {
  @Test
  void givenValidProfileAndUserWhenDeleteProfileThenProfileIsDeleted() throws Exception {
    mockMvc
      .perform(delete("/api/v1/scrapers/profiles/test-profile-0")
        .header("X-authentik-username", "tester-0"))
      .andExpect(status().isNoContent());

    Awaitility
      .await()
      .untilAsserted(() -> transaction(status -> {
        Assertions.assertTrue(scraperOwnedProfileRepository
          .findById(new ScraperOwnedProfileReference("tester-0", "test-profile-0"))
          .isEmpty());
        Assertions.assertTrue(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-0", "test-specification-0")));
        Assertions.assertTrue(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-0", "test-specification-1")));
        Assertions.assertTrue(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-1", "test-specification-0")));
        Assertions.assertTrue(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-1", "test-specification-1")));
      }));
  }

  @Test
  void givenProfileUsedInStrategyWhenDeleteProfileThenNoProfileDeleted() throws Exception {
    mockMvc
      .perform(delete("/api/v1/scrapers/profiles/test-profile-9")
        .header("X-authentik-username", "tester-9"))
      .andExpect(status().isConflict())
      .andExpect(content().string(
        EntityConflictExceptionReportConverter.MESSAGE.formatted(
          NullnessUtil
            .castNonNull(ScraperOwnedProfileEntity.class)
            .getSimpleName(),
          new ScraperOwnedProfileByReferrerFilter(new ScraperOwnedProfileReference("tester-9", "test-profile-9")))));
  }

  @Test
  void givenIncorrectOwnerWhenDeleteProfileThenNoProfileDeleted() throws Exception {
    mockMvc
      .perform(delete("/api/v1/scrapers/profiles/test-profile-0")
        .header("X-authentik-username", "tester-2"))
      .andExpect(status().isNotFound())
      .andExpect(content().string(
        EntityNotFoundExceptionReportConverter.MESSAGE.formatted(
          NullnessUtil
            .castNonNull(ScraperOwnedProfileEntity.class)
            .getSimpleName(),
          new ScraperOwnedProfileByReferrerFilter(new ScraperOwnedProfileReference("tester-2", "test-profile-0")))));
  }

  @Test
  void givenIncorrectProfileNameWhenDeleteProfileThenNoProfileDeleted() throws Exception {
    mockMvc
      .perform(delete("/api/v1/scrapers/profiles/test-profile-3")
        .header("X-authentik-username", "tester-0"))
      .andExpect(status().isNotFound())
      .andExpect(content().string(
        EntityNotFoundExceptionReportConverter.MESSAGE.formatted(
          NullnessUtil
            .castNonNull(ScraperOwnedProfileEntity.class)
            .getSimpleName(),
          new ScraperOwnedProfileByReferrerFilter(new ScraperOwnedProfileReference("tester-0", "test-profile-3")))));
  }

  @Test
  void givenNoAuthWhenDeleteProfileThenUnauthorized() throws Exception {
    mockMvc
      .perform(delete("/api/v1/scrapers/profiles/test-profile-0"))
      .andExpect(status().isUnauthorized());
  }

  @Test
  void givenForbiddenGroupWhenDeleteProfileThenForbidden() throws Exception {
    mockMvc
      .perform(delete("/api/v1/scrapers/profiles/test-profile-0")
        .header("X-authentik-username", "other"))
      .andExpect(status().isForbidden());
  }

  @Test
  void givenValidUserWhenDeleteAllProfilesThenProfilesDeleted() throws Exception {
    mockMvc
      .perform(delete("/api/v1/scrapers/profiles")
        .header("X-authentik-username", "tester-0"))
      .andExpect(status().isNoContent());

    Awaitility
      .await()
      .untilAsserted(() -> transaction(status -> {
        Assertions.assertTrue(scraperOwnedProfileRepository
          .findAllByReferenceOwner("tester-0")
          .isEmpty());
        Assertions.assertTrue(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-0", "test-specification-0")));
        Assertions.assertFalse(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-0", "test-specification-1")));
        Assertions.assertTrue(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-1", "test-specification-0")));
        Assertions.assertTrue(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-1", "test-specification-1")));
      }));
  }

  @Test
  void givenProfilesUsedInStrategyWhenDeleteAllProfilesThenNoProfilesDeleted() throws Exception {
    mockMvc
      .perform(delete("/api/v1/scrapers/profiles")
        .header("X-authentik-username", "tester-9"))
      .andExpect(status().isConflict())
      .andExpect(content().string(
        EntityConflictExceptionReportConverter.MESSAGE.formatted(
          NullnessUtil
            .castNonNull(ScraperOwnedProfileEntity.class)
            .getSimpleName(),
          new ScraperOwnedProfileBatchByReferenceOwnerFilter("tester-9"))));
  }

  @Test
  void givenNoProfilesWhenDeleteAllProfilesThenNoProfilesDeleted() throws Exception {
    mockMvc
      .perform(delete("/api/v1/scrapers/profiles")
        .header("X-authentik-username", "tester-2"))
      .andExpect(status().isNoContent());

    Awaitility
      .await()
      .untilAsserted(() -> transaction(status -> {
        Assertions.assertTrue(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-0", "test-specification-0")));
        Assertions.assertTrue(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-0", "test-specification-1")));
        Assertions.assertTrue(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-1", "test-specification-0")));
        Assertions.assertTrue(scraperOwnedSpecificationRepository
          .existsById(new ScraperOwnedSpecificationReference("tester-1", "test-specification-1")));
      }));
  }

  @Test
  void givenNoAuthWhenDeleteAllProfilesThenUnauthorized() throws Exception {
    mockMvc
      .perform(delete("/api/v1/scrapers/profiles"))
      .andExpect(status().isUnauthorized());
  }

  @Test
  void givenForbiddenGroupWhenDeleteAllProfilesThenForbidden() throws Exception {
    mockMvc
      .perform(delete("/api/v1/scrapers/profiles")
        .header("X-authentik-username", "other"))
      .andExpect(status().isForbidden());
  }

  @Test
  void givenValidProfileAndUserWhenFindProfileThenProfileReturned() throws Exception {
    mockMvc
      .perform(get("/api/v1/scrapers/profiles/test-profile-0")
        .header("X-authentik-username", "tester-0"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.owner").value("tester-0"))
      .andExpect(jsonPath("$.name").value("test-profile-0"))
      .andExpect(jsonPath("$.specification.owner").value("tester-0"))
      .andExpect(jsonPath("$.specification.name").value("test-specification-0"))
      .andExpect(jsonPath("$.inputs").isMap())
      .andExpect(jsonPath("$.inputs").isEmpty());
  }

  @Test
  void givenIncorrectOwnerWhenFindProfileThenNoProfileReturned() throws Exception {
    mockMvc
      .perform(get("/api/v1/scrapers/profiles/test-profile-0")
        .header("X-authentik-username", "tester-2"))
      .andExpect(status().isNotFound())
      .andExpect(content().string(
        EntityNotFoundExceptionReportConverter.MESSAGE.formatted(
          NullnessUtil
            .castNonNull(ScraperOwnedProfileEntity.class)
            .getSimpleName(),
          new ScraperOwnedProfileByReferrerFilter(new ScraperOwnedProfileReference("tester-2", "test-profile-0")))));
  }

  @Test
  void givenIncorrectProfileNameWhenFindProfileThenNoProfileReturned() throws Exception {
    mockMvc
      .perform(get("/api/v1/scrapers/profiles/test-profile-3")
        .header("X-authentik-username", "tester-0"))
      .andExpect(status().isNotFound())
      .andExpect(content().string(
        EntityNotFoundExceptionReportConverter.MESSAGE.formatted(
          NullnessUtil
            .castNonNull(ScraperOwnedProfileEntity.class)
            .getSimpleName(),
          new ScraperOwnedProfileByReferrerFilter(new ScraperOwnedProfileReference("tester-0", "test-profile-3")))));
  }

  @Test
  void givenNoAuthWhenFindProfileThenUnauthorized() throws Exception {
    mockMvc
      .perform(get("/api/v1/scrapers/profiles/test-profile-0"))
      .andExpect(status().isUnauthorized());
  }

  @Test
  void givenForbiddenGroupWhenFindProfileThenForbidden() throws Exception {
    mockMvc
      .perform(get("/api/v1/scrapers/profiles/test-profile-0")
        .header("X-authentik-username", "other"))
      .andExpect(status().isForbidden());
  }

  @Test
  void givenValidProfileAndUserWhenFindProfileRequirementsThenProfileRequirementsReturned() throws Exception {
    mockMvc
      .perform(get("/api/v1/scrapers/profiles/test-profile-0/requirements")
        .header("X-authentik-username", "tester-0"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.length()").value("3"))
      .andExpect(jsonPath("$[0].name").value("description"))
      .andExpect(jsonPath("$[0].type").value("text"))
      .andExpect(jsonPath("$[1].name").value("brand_description"))
      .andExpect(jsonPath("$[1].type").value("text"))
      .andExpect(jsonPath("$[2].name").value("email"))
      .andExpect(jsonPath("$[2].type").value("email"));
  }

  @Test
  void givenIncorrectOwnerWhenFindProfileRequirementsThenNoProfileRequirementsReturned() throws Exception {
    mockMvc
      .perform(get("/api/v1/scrapers/profiles/test-profile-0/requirements")
        .header("X-authentik-username", "tester-2"))
      .andExpect(status().isNotFound())
      .andExpect(content().string(
        EntityNotFoundExceptionReportConverter.MESSAGE.formatted(
          NullnessUtil
            .castNonNull(ScraperOwnedProfileEntity.class)
            .getSimpleName(),
          new ScraperOwnedProfileByReferrerFilter(new ScraperOwnedProfileReference("tester-2", "test-profile-0")))));
  }

  @Test
  void givenIncorrectProfileNameWhenFindProfileRequirementsThenNoProfileRequirementsReturned() throws Exception {
    mockMvc
      .perform(get("/api/v1/scrapers/profiles/test-profile-3/requirements")
        .header("X-authentik-username", "tester-0"))
      .andExpect(status().isNotFound())
      .andExpect(content().string(
        EntityNotFoundExceptionReportConverter.MESSAGE.formatted(
          NullnessUtil
            .castNonNull(ScraperOwnedProfileEntity.class)
            .getSimpleName(),
          new ScraperOwnedProfileByReferrerFilter(new ScraperOwnedProfileReference("tester-0", "test-profile-3")))));
  }

  @Test
  void givenNoAuthWhenFindProfileRequirementsThenUnauthorized() throws Exception {
    mockMvc
      .perform(get("/api/v1/scrapers/profiles/test-profile-0/requirements"))
      .andExpect(status().isUnauthorized());
  }

  @Test
  void givenForbiddenGroupWhenFindProfileRequirementsThenForbidden() throws Exception {
    mockMvc
      .perform(get("/api/v1/scrapers/profiles/test-profile-0/requirements")
        .header("X-authentik-username", "other"))
      .andExpect(status().isForbidden());
  }

  @Test
  void givenValidUserWhenFindAllProfilesThenProfilesReturned() throws Exception {
    final var expectedCount = 3;
    mockMvc
      .perform(get("/api/v1/scrapers/profiles")
        .header("X-authentik-username", "tester-0"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.length()").value(expectedCount))
      .andExpect(jsonPath("$[0].owner").value("tester-0"))
      .andExpect(jsonPath("$[0].name").value("test-profile-0"))
      .andExpect(jsonPath("$[0].specification.owner").value("tester-0"))
      .andExpect(jsonPath("$[0].specification.name").value("test-specification-0"))
      .andExpect(jsonPath("$[0].inputs").isMap())
      .andExpect(jsonPath("$[0].inputs").isEmpty())
      .andExpect(jsonPath("$[1].owner").value("tester-0"))
      .andExpect(jsonPath("$[1].name").value("test-profile-1"))
      .andExpect(jsonPath("$[1].specification.owner").value("tester-0"))
      .andExpect(jsonPath("$[1].specification.name").value("test-specification-1"))
      .andExpect(jsonPath("$[1].inputs").isMap())
      .andExpect(jsonPath("$[1].inputs").isEmpty())
      .andExpect(jsonPath("$[2].owner").value("tester-0"))
      .andExpect(jsonPath("$[2].name").value("test-profile-2"))
      .andExpect(jsonPath("$[2].specification.owner").value("tester-1"))
      .andExpect(jsonPath("$[2].specification.name").value("test-specification-1"))
      .andExpect(jsonPath("$[2].inputs").isMap())
      .andExpect(jsonPath("$[2].inputs").isEmpty());
  }

  @Test
  void givenNoProfilesWhenFindAllProfilesThenNoProfilesReturned() throws Exception {
    mockMvc
      .perform(get("/api/v1/scrapers/profiles")
        .header("X-authentik-username", "tester-2"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void givenNoAuthWhenFindAllProfilesThenUnauthorized() throws Exception {
    mockMvc
      .perform(get("/api/v1/scrapers/profiles"))
      .andExpect(status().isUnauthorized());
  }

  @Test
  void givenForbiddenGroupWhenFindAllProfilesThenForbidden() throws Exception {
    mockMvc
      .perform(get("/api/v1/scrapers/profiles")
        .header("X-authentik-username", "other"))
      .andExpect(status().isForbidden());
  }

  @Test
  void givenValidProfileDataWhenSaveProfileThenProfileCreated() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"name\":\"test-profile-3\",\"specification\":{\"owner\":\"tester-0\",\"name\":\"test-specification-0\"},\"inputs\":{\"description\":\"test\",\"brand_description\":\"test\",\"email\":\"test@test.com\"}}";

    mockMvc
      .perform(put("/api/v1/scrapers/profiles")
        .header("X-authentik-username", "tester-0")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isCreated())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.owner").value("tester-0"))
      .andExpect(jsonPath("$.name").value("test-profile-3"))
      .andExpect(jsonPath("$.specification.owner").value("tester-0"))
      .andExpect(jsonPath("$.specification.name").value("test-specification-0"))
      .andExpect(jsonPath("$.inputs").isMap())
      .andExpect(jsonPath("$.inputs.description").value("test"))
      .andExpect(jsonPath("$.inputs.brand_description").value("test"))
      .andExpect(jsonPath("$.inputs.email").value("test@test.com"));

    transaction(status -> {
      final var expectedCount = 4;
      Assertions.assertEquals(expectedCount, scraperOwnedProfileRepository
        .findAllByReferenceOwner("tester-0")
        .size());
      final var entity = scraperOwnedProfileRepository
        .findById(new ScraperOwnedProfileReference("tester-0", "test-profile-3"));
      Assertions.assertTrue(entity.isPresent());
      final var scraperOwnedProfileEntity = entity.get();
      final var reference = scraperOwnedProfileEntity.getReference();
      final var specification = scraperOwnedProfileEntity
        .getSpecification()
        .getReference();
      Assertions.assertEquals("tester-0", reference.owner());
      Assertions.assertEquals("test-profile-3", reference.name());
      Assertions.assertEquals("tester-0", specification.owner());
      Assertions.assertEquals("test-specification-0", specification.name());
      Assertions.assertEquals("test", scraperOwnedProfileEntity
        .getInputs()
        .get("description"));
      Assertions.assertEquals("test", scraperOwnedProfileEntity
        .getInputs()
        .get("brand_description"));
      Assertions.assertEquals("test@test.com", scraperOwnedProfileEntity
        .getInputs()
        .get("email"));
    });
  }

  @Test
  void givenMissingRequirementWhenSaveProfileThenBadRequest() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"name\":\"test-profile-3\",\"specification\":{\"owner\":\"tester-0\",\"name\":\"test-specification-0\"},\"inputs\":{\"brand_description\":\"test\",\"email\":\"test@test.com\"}}";

    mockMvc
      .perform(put("/api/v1/scrapers/profiles")
        .header("X-authentik-username", "tester-0")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.length()").value(1))
      .andExpect(jsonPath("$[0].property").value("inputs.description"))
      .andExpect(jsonPath("$[0].message").value("description is required"));
  }

  @Test
  void givenInvalidRequirementWhenSaveProfileThenBadRequest() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"name\":\"test-profile-3\",\"specification\":{\"owner\":\"tester-0\",\"name\":\"test-specification-0\"},\"inputs\":{\"description\":\"test\",\"brand_description\":\"test\",\"email\":\"test\"}}";

    mockMvc
      .perform(put("/api/v1/scrapers/profiles")
        .header("X-authentik-username", "tester-0")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.length()").value(1))
      .andExpect(jsonPath("$[0].property").value("inputs.email"))
      .andExpect(jsonPath("$[0].message").value("invalid email format"));
  }

  @Test
  void givenMissingSpecificationOwnerWhenSaveProfileThenBadRequest() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"name\":\"test-profile-3\",\"specification\":{\"owner\":\"\",\"name\":\"test-specification-0\"},\"inputs\":{}}";

    mockMvc
      .perform(put("/api/v1/scrapers/profiles")
        .header("X-authentik-username", "tester-0")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.length()").value(1))
      .andExpect(jsonPath("$[0].property").value("specification"))
      .andExpect(jsonPath("$[0].message").value("specification does not exist"));
  }

  @Test
  void givenMissingSpecificationNameWhenSaveProfileThenBadRequest() throws Exception {
    mockMvc
      .perform(put("/api/v1/scrapers/profiles")
        .header("X-authentik-username", "tester-0")
        .contentType(MediaType.APPLICATION_JSON)
        .content(
          "{\"name\":\"test-profile-3\",\"specification\":{\"owner\":\"tester-0\",\"name\":\"\"},\"inputs\":{}}"))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.length()").value(1))
      .andExpect(jsonPath("$[0].property").value("specification"))
      .andExpect(jsonPath("$[0].message").value("specification does not exist"));
  }

  @Test
  void givenDeletedSpecificationWhenSaveProfileThenBadRequest() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"name\":\"test-profile-3\",\"specification\":{\"owner\":\"tester-0\",\"name\":\"test-specification-1\"},\"inputs\":{\"description\":\"test\",\"brand_description\":\"test\",\"email\":\"test@test.com\"}}";

    mockMvc
      .perform(put("/api/v1/scrapers/profiles")
        .header("X-authentik-username", "tester-0")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.length()").value(1))
      .andExpect(jsonPath("$[0].property").value("specification"))
      .andExpect(jsonPath("$[0].message").value("specification does not exist"));
  }

  @Test
  void givenExistingProfileReferenceWhenSaveProfileThenConflict() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"name\":\"test-profile-0\",\"specification\":{\"owner\":\"tester-0\",\"name\":\"test-specification-0\"},\"inputs\":{\"description\":\"test\",\"brand_description\":\"test\",\"email\":\"test@test.com\"}}";

    mockMvc
      .perform(put("/api/v1/scrapers/profiles")
        .header("X-authentik-username", "tester-0")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.length()").value(1))
      .andExpect(jsonPath("$[0].property").value("name"))
      .andExpect(jsonPath("$[0].message").value("profile already exists"));
  }

  @Test
  void givenInvalidProfileNameWhenSaveProfileThenBadRequest() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"name\":\"test/profile-0\",\"specification\":{\"owner\":\"tester-0\",\"name\":\"test-specification-0\"},\"inputs\":{\"description\":\"test\",\"brand_description\":\"test\",\"email\":\"test@test.com\"}}";

    mockMvc
      .perform(put("/api/v1/scrapers/profiles")
        .header("X-authentik-username", "tester-0")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.length()").value(1))
      .andExpect(jsonPath("$[0].property").value("name"))
      .andExpect(jsonPath("$[0].message").value(Matchers.startsWith("must match")));
  }

  @Test
  void givenNoAuthWhenSaveProfileThenUnauthorized() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"name\":\"test-profile-3\",\"specification\":{\"owner\":\"tester-0\",\"name\":\"test-specification-0\"},\"inputs\":{\"description\":\"test\",\"brand_description\":\"test\",\"email\":\"test@test.com\"}}";

    mockMvc
      .perform(put("/api/v1/scrapers/profiles")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isUnauthorized());
  }

  @Test
  void givenForbiddenGroupWhenSaveProfileThenForbidden() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"name\":\"test-profile-3\",\"specification\":{\"owner\":\"tester-0\",\"name\":\"test-specification-0\"},\"inputs\":{\"description\":\"test\",\"brand_description\":\"test\",\"email\":\"test@test.com\"}}";

    mockMvc
      .perform(put("/api/v1/scrapers/profiles")
        .header("X-authentik-username", "other")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isForbidden());
  }

  @Test
  void givenValidUpdateDataWhenUpdateProfileThenProfileUpdated() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"specification\":{\"owner\":\"tester-1\",\"name\":\"test-specification-0\"},\"inputs\":{\"description\":\"test\",\"brand_description\":\"test\",\"email\":\"test@test.com\"}}";

    mockMvc
      .perform(patch("/api/v1/scrapers/profiles/test-profile-2")
        .header("X-authentik-username", "tester-0")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.owner").value("tester-0"))
      .andExpect(jsonPath("$.name").value("test-profile-2"))
      .andExpect(jsonPath("$.specification.owner").value("tester-1"))
      .andExpect(jsonPath("$.specification.name").value("test-specification-0"))
      .andExpect(jsonPath("$.inputs.description").value("test"))
      .andExpect(jsonPath("$.inputs.brand_description").value("test"))
      .andExpect(jsonPath("$.inputs.email").value("test@test.com"));

    transaction(status -> {
      final var expectedCount = 3;
      Assertions.assertEquals(expectedCount, scraperOwnedProfileRepository
        .findAllByReferenceOwner("tester-0")
        .size());
      final var entity = scraperOwnedProfileRepository
        .findById(new ScraperOwnedProfileReference("tester-0", "test-profile-2"));
      Assertions.assertTrue(entity.isPresent());
      final var scraperOwnedProfileEntity = entity.get();
      final var reference = scraperOwnedProfileEntity.getReference();
      final var specification = scraperOwnedProfileEntity
        .getSpecification()
        .getReference();
      Assertions.assertEquals("tester-0", reference.owner());
      Assertions.assertEquals("test-profile-2", reference.name());
      Assertions.assertEquals("tester-1", specification.owner());
      Assertions.assertEquals("test-specification-0", specification.name());
      Assertions.assertEquals("test", scraperOwnedProfileEntity
        .getInputs()
        .get("description"));
      Assertions.assertEquals("test", scraperOwnedProfileEntity
        .getInputs()
        .get("brand_description"));
      Assertions.assertEquals("test@test.com", scraperOwnedProfileEntity
        .getInputs()
        .get("email"));
    });
  }

  @Test
  void givenMissingRequirementWhenUpdateProfileThenBadRequest() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"specification\":{\"owner\":\"tester-1\",\"name\":\"test-specification-0\"},\"inputs\":{\"brand_description\":\"test\",\"email\":\"test@test.com\"}}";

    mockMvc
      .perform(patch("/api/v1/scrapers/profiles/test-profile-2")
        .header("X-authentik-username", "tester-0")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.length()").value(1))
      .andExpect(jsonPath("$[0].property").value("inputs.description"))
      .andExpect(jsonPath("$[0].message").value("description is required"));
  }

  @Test
  void givenInvalidRequirementWhenUpdateProfileThenBadRequest() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"specification\":{\"owner\":\"tester-1\",\"name\":\"test-specification-0\"},\"inputs\":{\"description\":\"test\",\"brand_description\":\"test\",\"email\":\"test\"}}";

    mockMvc
      .perform(patch("/api/v1/scrapers/profiles/test-profile-2")
        .header("X-authentik-username", "tester-0")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.length()").value(1))
      .andExpect(jsonPath("$[0].property").value("inputs.email"))
      .andExpect(jsonPath("$[0].message").value("invalid email format"));
  }

  @Test
  void givenIncorrectOwnerWhenUpdateProfileThenNotFound() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"specification\":{\"owner\":\"tester-1\",\"name\":\"test-specification-0\"},\"inputs\":{\"description\":\"test\",\"brand_description\":\"test\",\"email\":\"test@test.com\"}}";

    mockMvc
      .perform(patch("/api/v1/scrapers/profiles/test-profile-0")
        .header("X-authentik-username", "tester-2")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.length()").value(1))
      .andExpect(jsonPath("$[0].property").value("name"))
      .andExpect(jsonPath("$[0].message").value("profile does not exist"));
  }

  @Test
  void givenIncorrectProfileNameWhenUpdateProfileThenNotFound() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"specification\":{\"owner\":\"tester-1\",\"name\":\"test-specification-0\"},\"inputs\":{\"description\":\"test\",\"brand_description\":\"test\",\"email\":\"test@test.com\"}}";

    mockMvc
      .perform(patch("/api/v1/scrapers/profiles/test-profile-3")
        .header("X-authentik-username", "tester-0")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.length()").value(1))
      .andExpect(jsonPath("$[0].property").value("name"))
      .andExpect(jsonPath("$[0].message").value("profile does not exist"));
  }

  @Test
  void givenDeletedSpecificationWhenUpdateProfileThenBadRequest() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"specification\":{\"owner\":\"tester-0\",\"name\":\"test-specification-1\"},\"inputs\":{\"description\":\"test\",\"brand_description\":\"test\",\"email\":\"test@test.com\"}}";

    mockMvc
      .perform(patch("/api/v1/scrapers/profiles/test-profile-2")
        .header("X-authentik-username", "tester-0")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.length()").value(1))
      .andExpect(jsonPath("$[0].property").value("specification"))
      .andExpect(jsonPath("$[0].message").value(Matchers.startsWith("specification does not exist")));
  }

  @Test
  void givenMissingSpecificationOwnerWhenUpdateProfileThenBadRequest() throws Exception {
    mockMvc
      .perform(patch("/api/v1/scrapers/profiles/test-profile-2")
        .header("X-authentik-username", "tester-0")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"specification\":{\"owner\":\"\",\"name\":\"test-specification-0\"},\"inputs\":{}}"))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.length()").value(1))
      .andExpect(jsonPath("$[0].property").value("specification"))
      .andExpect(jsonPath("$[0].message").value(Matchers.startsWith("specification does not exist")));
  }

  @Test
  void givenMissingSpecificationNameWhenUpdateProfileThenBadRequest() throws Exception {
    mockMvc
      .perform(patch("/api/v1/scrapers/profiles/test-profile-2")
        .header("X-authentik-username", "tester-0")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"specification\":{\"owner\":\"tester-0\",\"name\":\"\"},\"inputs\":{}}"))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.length()").value(1))
      .andExpect(jsonPath("$[0].property").value("specification"))
      .andExpect(jsonPath("$[0].message").value(Matchers.startsWith("specification does not exist")));
  }

  @Test
  void givenNoAuthWhenUpdateProfileThenUnauthorized() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"specification\":{\"owner\":\"tester-1\",\"name\":\"test-specification-1\"},\"inputs\":{\"description\":\"test\",\"brand_description\":\"test\",\"email\":\"test@test.com\"}}";

    mockMvc
      .perform(patch("/api/v1/scrapers/profiles/test-profile-2")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isUnauthorized());
  }

  @Test
  void givenForbiddenGroupWhenUpdateProfileThenForbidden() throws Exception {
    @SuppressWarnings("LineLength") final var content =
      "{\"specification\":{\"owner\":\"tester-1\",\"name\":\"test-specification-1\"},\"inputs\":{\"description\":\"test\",\"brand_description\":\"test\",\"email\":\"test@test.com\"}}";

    mockMvc
      .perform(patch("/api/v1/scrapers/profiles/test-profile-2")
        .header("X-authentik-username", "other")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
      .andExpect(status().isForbidden());
  }
}
