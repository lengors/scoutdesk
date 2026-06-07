package io.github.lengors.scoutdesk.domain.spring.security.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * An authentication entry point that sends a specified HTTP status code when authentication is required.
 * <p>
 * This class implements the {@link AuthenticationEntryPoint} interface and is used to handle unauthorized access *
 * attempts by sending a predefined HTTP status code and reason phrase.
 *
 * @author lengors
 */
public class HttpStatusEntryPoint implements AuthenticationEntryPoint {
  private final HttpStatusCode httpStatusCode;

  /**
   * Creates a new instance of {@link HttpStatusEntryPoint} with the specified HTTP status.
   *
   * @param httpStatusCode the HTTP status code to send when authentication is required
   */
  public HttpStatusEntryPoint(final HttpStatusCode httpStatusCode) {
    this.httpStatusCode = httpStatusCode;
  }

  /**
   * Commences an authentication scheme.
   * <p>
   * <code>ExceptionTranslationFilter</code> will populate the <code>HttpSession</code> attribute named
   * <code>AbstractAuthenticationProcessingFilter.SPRING_SECURITY_SAVED_REQUEST_KEY</code> with the requested target
   * URL
   * before calling this method.
   * <p>
   * Implementations should modify the headers on the <code>ServletResponse</code> as necessary to commence the
   * authentication process.
   *
   * @param request       that resulted in an <code>AuthenticationException</code>
   * @param response      so that the user agent can begin authentication
   * @param authException that caused the invocation
   */
  @Override
  public void commence(
    final HttpServletRequest request,
    final HttpServletResponse response,
    final AuthenticationException authException
  ) throws IOException {
    if (httpStatusCode instanceof HttpStatus httpStatus) {
      response.sendError(
        httpStatusCode.value(),
        httpStatus.getReasonPhrase());
    } else {
      response.sendError(httpStatusCode.value());
    }
  }
}
