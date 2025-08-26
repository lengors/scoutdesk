package io.github.lengors.scoutdesk.domain.spring.security.services;

import org.springframework.security.web.authentication.AuthenticationConverter;

/**
 * Interface for converting HTTP requests into authentication objects.
 * <p>
 * This interface is implemented to handle custom authentication logic.
 *
 * @author lengors
 */
public interface ProxiedAuthenticationConverter extends AuthenticationConverter {

}
