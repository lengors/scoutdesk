package io.github.lengors.scoutdesk.domain.spring.security.services;

import jakarta.servlet.Filter;

/**
 * Interface for a filter that handles impersonation in proxied authentication scenarios. Implementations can provide
 * specific logic for handling impersonation requests.
 *
 * @author lengors
 */
public interface ProxiedAuthenticationImpersonationFilter extends Filter {

}
