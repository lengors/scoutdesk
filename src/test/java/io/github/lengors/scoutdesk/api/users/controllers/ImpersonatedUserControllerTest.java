package io.github.lengors.scoutdesk.api.users.controllers;

import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories.ScraperOwnedSpecificationRepository;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories.ScraperOwnedStrategyRepository;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;
import io.github.lengors.scoutdesk.testing.utilities.ResourceUtils;
import io.github.lengors.scoutdesk.testing.utilities.TestSuite;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestSuite.Defaults
@TestPropertySource(properties = "authentik.impersonated-user=tester-0")
record ImpersonatedUserControllerTest(
  @Autowired MockMvc mockMvc,
  @Autowired ResourceUtils resourceUtils,
  @Autowired WebscoutRestClient webscoutRestClient,
  @Autowired PlatformTransactionManager platformTransactionManager,
  @Autowired ScraperOwnedProfileRepository scraperOwnedProfileRepository,
  @Autowired ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository,
  @Autowired ScraperOwnedStrategyRepository scraperOwnedStrategyRepository
) implements TestSuite {
  @Test
  void givenRequestWithoutUsernameHeaderWhenFindUserThenUserIsFound() throws Exception {
    mockMvc
      .perform(get("/api/v1/users/me"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.username").value("tester-0"))
      .andExpect(jsonPath("$.name").value("Tester 0"))
      .andExpect(jsonPath("$.roles.length()").value("1"))
      .andExpect(jsonPath("$.roles[0]").value("developer"))
      .andExpect(jsonPath("$.email").value("tester-0@example.com"))
      .andExpect(jsonPath("$.avatar").value("https://example.com/avatar.png"));
  }
}
