package io.github.lengors.scoutdesk.api.scrapers.profiles.models;

import java.util.Map;

import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileReference;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;

/**
 * This class is used to test the {@link ScraperOwnedProfileEntity} class by
 * instantiating it with a {@link ScraperOwnedSpecificationReference} and a
 * {@link ScraperOwnedProfileReference}.
 *
 * @param specificationReference the {@link ScraperOwnedSpecificationReference}
 *                               to be used in the test
 * @param profileReference       the {@link ScraperOwnedProfileReference} to be
 *                               used in the test
 * @param inputs                 the inputs to be used in the test
 *
 * @author lengors
 */
public record ScraperOwnedProfileTestingEntity(
    ScraperOwnedSpecificationReference specificationReference,
    ScraperOwnedProfileReference profileReference,
    Map<String, String> inputs) {

  /**
   * This constructor is used to create a new instance of the class with the
   * specified owner, name, and inputs.
   *
   * @param specificationOwner the owner of the specification
   * @param specificationName  the name of the specification
   * @param profileOwner       the owner of the profile
   * @param profileName        the name of the profile
   * @param inputs             the inputs to be used in the test
   */
  public ScraperOwnedProfileTestingEntity(
      final String specificationOwner,
      final String specificationName,
      final String profileOwner,
      final String profileName,
      final Map<String, String> inputs) {
    this(
        new ScraperOwnedSpecificationReference(specificationOwner, specificationName),
        new ScraperOwnedProfileReference(profileOwner, profileName),
        inputs);
  }

  /**
   * This constructor is used to create a new instance of the class with the
   * specified owner, name, and inputs.
   *
   * @param owner         the owner of the specification
   * @param specification the name of the specification
   * @param profileName   the name of the profile
   * @param inputs        the inputs to be used in the test
   */
  public ScraperOwnedProfileTestingEntity(
      final String owner,
      final String specificationName,
      final String profileName,
      final Map<String, String> inputs) {
    this(owner, specificationName, owner, profileName, inputs);
  }

  /**
   * This constructor is used to create a new instance of the class with the
   * specified {@link ScraperOwnedSpecificationReference} and a null status.
   *
   * @param specificationReference the {@link ScraperOwnedSpecificationReference}
   *                               to be used in the test
   * @param profileReference       the {@link ScraperOwnedProfileReference} to be
   *                               used in the test
   */
  public ScraperOwnedProfileTestingEntity(
      final ScraperOwnedSpecificationReference specificationReference,
      final ScraperOwnedProfileReference profileReference) {
    this(specificationReference, profileReference, Map.of());
  }

  /**
   * This constructor is used to create a new instance of the class with the
   * specified owner and name, setting the status to null.
   *
   * @param specificationOwner the owner of the specification
   * @param specificationName  the name of the specification
   * @param profileOwner       the owner of the profile
   * @param profileName        the name of the profile
   */
  public ScraperOwnedProfileTestingEntity(
      final String specificationOwner,
      final String specificationName,
      final String profileOwner,
      final String profileName) {
    this(specificationOwner, specificationName, profileOwner, profileName, Map.of());
  }

  /**
   * This constructor is used to create a new instance of the class with the
   * specified owner and name, setting the status to null.
   *
   * @param owner         the owner of the specification
   * @param specification the name of the specification
   * @param profileName   the name of the profile
   */
  public ScraperOwnedProfileTestingEntity(
      final String owner,
      final String specificationName,
      final String profileName) {
    this(owner, specificationName, profileName, Map.of());
  }
}
