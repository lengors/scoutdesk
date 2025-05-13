package io.github.lengors.scoutdesk.domain.spring.security.services;

import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Service;

/**
 * Interface for converting HTTP requests into authentication objects.
 *
 * This interface is implemented to handle custom authentication logic.
 *
 * @author lengors
 */
@Service
public interface ProxiedAuthenticationConverter extends AuthenticationConverter {

}
