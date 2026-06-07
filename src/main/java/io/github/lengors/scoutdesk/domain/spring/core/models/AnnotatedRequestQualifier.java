package io.github.lengors.scoutdesk.domain.spring.core.models;

import org.springframework.web.bind.annotation.RequestPart;

/**
 * A record that provides an implementation of the {@link RequestQualifier} interface by utilizing a {@link RequestPart}
 * annotation.
 * <p>
 * This class encapsulates a {@link RequestPart} annotation and retrieves its name for use as a request qualifier. It is
 * often used to bind request parts to specific qualifiers in scenarios like handling multipart request bodies.
 *
 * @param requestPart The {@link RequestPart} annotation that this qualifier is based on.
 * @author lengors
 */
public record AnnotatedRequestQualifier(RequestPart requestPart) implements RequestQualifier {
  @Override
  public String name() {
    return requestPart.name();
  }
}
