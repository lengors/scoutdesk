package io.github.lengors.scoutdesk.domain.spring.security.services;

import jakarta.servlet.Filter;
import org.springframework.stereotype.Component;

/**
 * Interface for a filter that handles impersonation in proxied authentication scenarios. Implementations can provide
 * specific logic for handling impersonation requests.
 *
 * @author lengors
 */
@Component
public interface ProxiedAuthenticationImpersonationFilter extends Filter {

}
