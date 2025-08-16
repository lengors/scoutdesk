package io.github.lengors.scoutdesk.integrations.authentik.properties;

import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;

/**
 * Represents the connection details for the Authentik client.
 * <p>
 * This interface extends {@link ConnectionDetails} to provide specific properties required for connecting to the
 * Authentik service.
 * <p>
 * Implementations of this interface should provide the service account token and the URL of the Authentik service.
 *
 * @author lengors
 */
public interface AuthentikClientConnectionDetails extends ConnectionDetails {

  /**
   * Returns the service account token used for authentication with the Authentik service.
   *
   * @return the service account token
   */
  String serviceAccountToken();

  /**
   * Returns the URL of the Authentik service.
   *
   * @return the URL of the Authentik service
   */
  String url();
}
