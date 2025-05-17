package io.github.lengors.scoutdesk.domain.text.exceptions.models;

/**
 * Exception thrown when an invalid character is found in a string.
 *
 * This exception is used to indicate that a string contains a character that is
 * not allowed or recognized.
 *
 * @author lengors
 */
public class InvalidCharacterException extends Exception {

  /**
   * Constructor for creating an exception with a specific invalid character.
   *
   * @param character The invalid character that was found.
   */
  public InvalidCharacterException(final char character) {
    super(String.format("Invalid character found: '%s'", character));
  }
}
