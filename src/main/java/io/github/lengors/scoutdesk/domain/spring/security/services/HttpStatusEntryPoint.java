package io.github.lengors.scoutdesk.domain.spring.security.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * An authentication entry point that sends a specified HTTP status code when authentication is required.
 * <p>
 * This class implements the {@link AuthenticationEntryPoint} interface and is used to handle unauthorized access
 * attempts by sending a predefined HTTP status code and reason phrase.
 *
 * @author lengors
 */
public class HttpStatusEntryPoint implements AuthenticationEntryPoint {
  private final HttpStatus httpStatus;

  /**
   * Creates a new instance of {@link HttpStatusEntryPoint} with the specified HTTP status.
   *
   * @param httpStatus the HTTP status to send when authentication is required
   */
  public HttpStatusEntryPoint(final HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }

  /**
   * Commences an authentication scheme.
   * <p>
   * This method is called when an authentication exception is thrown, and it sends an error response with the
   * configured HTTP status code and reason phrase.
   *
   * @param request       the HTTP request
   * @param response      the HTTP response
   * @param authException the authentication exception that caused this invocation
   * @throws IOException if an input or output error occurs while sending the error response
   */
  @Override
  public void commence(
    final HttpServletRequest request,
    final HttpServletResponse response,
    final AuthenticationException authException
  ) throws IOException {
    response.sendError(httpStatus.value(), httpStatus.getReasonPhrase());
  }
}
