package io.github.lengors.scoutdesk.integrations.authentik.properties;

/**
 * Implementation of {@link AuthentikClientConnectionDetails} that holds the connection details for the Authentik client
 * based on properties.
 *
 * @param serviceAccountToken the service account token for authentication
 * @param url                 the URL of the Authentik server
 * @author lengors
 */
public record AuthentikClientConnectionDetailsProperties(
  String serviceAccountToken,
  String url
) implements AuthentikClientConnectionDetails {

}
