package io.github.lengors.scoutdesk.domain.core;

/**
 * Interface for providing a unique identifier. This interface does not concern itself with who uses it. Implementing
 * classes should provide a concrete implementation of the {@link #getUniqueIdentifier()} method to return a unique
 * identifier, which can be used for various purposes such as tracking, logging, or identifying instances uniquely.
 * <p>
 * This interface is designed to be implemented by classes that need to provide a unique identifier, which can be
 * different for each call to the method. The unique identifier can be of any type, such as a String, UUID, or any other
 * object that can serve as a unique identifier.
 *
 * @author lengors
 */
public interface UniqueIdentifierProvider {

  /**
   * Returns a unique identifier provided by the implementing class.
   * <p>
   * This method should be implemented to return a unique identifier that can be different for each call to this
   * method.
   *
   * @return a unique identifier provided by the implementing class
   */
  Object getUniqueIdentifier();
}
