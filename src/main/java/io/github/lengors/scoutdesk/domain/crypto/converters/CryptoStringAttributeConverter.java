package io.github.lengors.scoutdesk.domain.crypto.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.jasypt.encryption.StringEncryptor;

/**
 * Converter for encrypting and decrypting String-based attributes in JPA entities.
 * <p>
 * This class implements the {@link AttributeConverter} interface to handle attribute persistence in an encrypted format
 * within the database. It uses the provided {@code StringEncryptor} to perform encryption before storing data and
 * decryption when retrieving data from the database.
 *
 * @author lengors
 */
@Converter
public class CryptoStringAttributeConverter implements AttributeConverter<String, String> {
  private final StringEncryptor encryptor;

  CryptoStringAttributeConverter(final StringEncryptor encryptor) {
    this.encryptor = encryptor;
  }

  /**
   * Converts the given attribute value to its encrypted form before persisting it in the database.
   *
   * @param attribute the entity attribute value to be converted
   * @return the encrypted attribute value
   */
  @Override
  public String convertToDatabaseColumn(final String attribute) {
    return encryptor.encrypt(attribute);
  }

  /**
   * Converts the encrypted data from the database column to its original form when retrieving it from the database.
   *
   * @param dbData the data from the database column to be converted
   * @return the decrypted attribute value
   */
  @Override
  public String convertToEntityAttribute(final String dbData) {
    return encryptor.decrypt(dbData);
  }
}
