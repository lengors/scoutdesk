package io.github.lengors.scoutdesk.integrations.authentik.clients;

import io.github.lengors.scoutdesk.integrations.authentik.models.AuthentikPaginated;
import io.github.lengors.scoutdesk.integrations.authentik.models.AuthentikUser;
import io.github.lengors.scoutdesk.integrations.authentik.properties.AuthentikClientConnectionDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

/**
 * Client for interacting with Authentik's REST API.
 * <p>
 * This client provides methods to fetch paginated user data from Authentik.
 *
 * @author lengors
 */
@Component
public class AuthentikRestClient {
  private static final ParameterizedTypeReference<AuthentikPaginated<AuthentikUser>> PAGINATED_USER_TYPE_REFERENCE =
    new ParameterizedTypeReference<>() {
    };

  private static final Logger LOG = LoggerFactory.getLogger(AuthentikRestClient.class);

  private static final String USERS_PATH = "/api/v3/core/users/";

  private final RestClient restClient;

  AuthentikRestClient(
    final AuthentikClientConnectionDetails authentikClientConnectionDetails,
    final RestClient.Builder restClientBuilder
  ) {
    LOG.info(
      "Setting {} up: {url={}, serviceAccountToken={}}",
      getClass().getSimpleName(),
      authentikClientConnectionDetails.url(),
      authentikClientConnectionDetails.serviceAccountToken());
    this.restClient = restClientBuilder
      .baseUrl(authentikClientConnectionDetails.url())
      .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .defaultHeader(
        HttpHeaders.AUTHORIZATION,
        "Bearer %s".formatted(authentikClientConnectionDetails.serviceAccountToken()))
      .build();
  }

  /**
   * Finds a user by their username in Authentik.
   *
   * @param username the username of the user to find
   * @return the AuthentikUser object if found
   * @throws java.util.NoSuchElementException if no user is found with the given username
   */
  public AuthentikUser findUser(final String username) {
    int nextPage = 0;
    Optional<AuthentikUser> user;
    AuthentikPaginated<AuthentikUser> paginated;

    do {
      final var currentPage = nextPage;
      paginated = Optional
        .ofNullable(restClient
          .get()
          .uri(uriBuilder -> {
            uriBuilder = uriBuilder
              .path(USERS_PATH)
              .queryParam("username", username);
            if (currentPage != 0) {
              uriBuilder = uriBuilder.queryParam("page", currentPage);
            }
            return uriBuilder.build();
          })
          .retrieve()
          .body(PAGINATED_USER_TYPE_REFERENCE))
        .orElseThrow();

      user = paginated
        .results()
        .stream()
        .findAny();
    } while (user.isEmpty() && (
      nextPage =
        paginated
          .pagination()
          .next()) != 0);

    return user.orElseThrow();
  }
}
