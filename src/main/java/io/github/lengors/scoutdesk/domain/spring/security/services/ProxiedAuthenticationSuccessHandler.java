package io.github.lengors.scoutdesk.domain.spring.security.services;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles successful authentication events.
 *
 * This class logs the success of an authentication attempt.
 *
 * @author lengors
 */
public class ProxiedAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
  private static final Logger LOG = LoggerFactory.getLogger(ProxiedAuthenticationSuccessHandler.class);

  /**
   * Called when a user has been successfully authenticated.
   *
   * @param request        the HTTP request
   * @param response       the HTTP response
   * @param authentication the authentication object
   * @throws IOException      if an input or output error occurs
   * @throws ServletException if a servlet-specific error occurs
   */
  @Override
  public void onAuthenticationSuccess(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final Authentication authentication) throws IOException, ServletException {
    LOG.debug("Authentication successful for user: {}", authentication.getName());
  }
}
