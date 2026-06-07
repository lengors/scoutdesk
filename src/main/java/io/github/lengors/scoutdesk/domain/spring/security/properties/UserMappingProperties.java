package io.github.lengors.scoutdesk.domain.spring.security.properties;

import java.util.List;

/**
 * Represents a contract for defining mappings of user attributes such as username, name, email, and avatar. This
 * interface is designed to be implemented by classes or records that provide a structured way of mapping user-related
 * properties, typically in the context of authentication or user management systems.
 * <p>
 * Each method returns a list of strings that serves as the mapping for a specific user attribute. The mappings can
 * accommodate multiple values to allow flexibility when dealing with data from diverse sources or formats.
 *
 * @author lengors
 */
public interface UserMappingProperties {
  /**
   * Returns a list of strings that represent the mapping for the specified user attribute.
   *
   * @return a list of attribute names to map to the user's username
   */
  List<String> username();

  /**
   * Returns a list of strings that represent the mapping for the specified user attribute.
   *
   * @return a list of attribute names to map to the user's name
   */
  List<String> name();

  /**
   * Returns a list of strings that represent the mapping for the specified user attribute.
   *
   * @return a list of attribute names to map to the user's email
   */
  List<String> email();

  /**
   * Returns a list of strings that represent the mapping for the specified user attribute.
   *
   * @return a list of attribute names to map to the user's avatar URL
   */
  List<String> avatar();
}
