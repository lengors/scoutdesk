package io.github.lengors.scoutdesk.domain.scrapers.profiles.models;

import io.github.lengors.scoutdesk.domain.crypto.converters.CryptoStringAttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.TypeUseLocation;

/**
 * Represents an input used by the scraper.
 * <p>
 * This record encapsulates a single input value required for scraper operations. The value is stored in an encrypted
 * format in the database and decrypted when accessed at runtime.
 * <p>
 * An instance of this class is immutable, ensuring thread safety and consistency of its value. The encrypted storage is
 * handled by a custom JPA {@code AttributeConverter}.
 *
 * @param value the input value to be used by the scraper
 * @author lengors
 */
@Embeddable
@DefaultQualifier(value = Nullable.class, locations = {TypeUseLocation.FIELD, TypeUseLocation.PARAMETER})
public record ScraperInput(
  @Convert(converter = CryptoStringAttributeConverter.class)
  @Column(name = "value")
  @NotNull
  @NotBlank
  String value
) {
  @Override
  @NotNull
  public String toString() {
    return "ScraperInput(value='******')";
  }
}
