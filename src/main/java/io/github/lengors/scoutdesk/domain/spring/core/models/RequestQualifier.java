package io.github.lengors.scoutdesk.domain.spring.core.models;

/**
 * Represents a qualifier for a request, allowing components to associate themselves with a specific identifier or
 * name.
 * <p>
 * Implementations of this interface provide a {@link #name()} method to retrieve the associated name used as a
 * qualifier. It is commonly used in scenarios where differentiation or categorization of requests based on their
 * qualifiers is necessary.
 *
 * @author lengors
 */
public interface RequestQualifier {

  /**
   * Retrieves the name associated with this request qualifier.
   *
   * @return the name of the qualifier, typically used to differentiate or categorize requests.
   */
  String name();
}
