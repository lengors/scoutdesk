package io.github.lengors.scoutdesk.api.scrapers.specifications.models;

import org.checkerframework.checker.nullness.qual.Nullable;

import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;

/**
 * This class is used to test the {@link ScraperOwnedSpecificationEntity} class
 * by instantiating it with a {@link ScraperOwnedSpecificationReference} and a
 * {@link ScraperOwnedSpecificationStatus}.
 *
 * @param reference the {@link ScraperOwnedSpecificationReference} to be used in
 *                  the test
 * @param status    the {@link ScraperOwnedSpecificationStatus} to be used in
 *                  the test
 *
 * @author lengors
 */
public record ScraperOwnedSpecificationTestingEntity(
    ScraperOwnedSpecificationReference reference,
    @Nullable ScraperOwnedSpecificationStatus status) {

  /**
   * This constructor is used to create a new instance of the class with the
   * specified owner, name, and status.
   *
   * @param owner  the owner of the specification
   * @param name   the name of the specification
   * @param status the status of the specification
   */
  public ScraperOwnedSpecificationTestingEntity(
      final String owner,
      final String name,
      final @Nullable ScraperOwnedSpecificationStatus status) {
    this(new ScraperOwnedSpecificationReference(owner, name), status);
  }

  /**
   * This constructor is used to create a new instance of the class with the
   * specified owner and name, setting the status to null.
   *
   * @param owner the owner of the specification
   * @param name  the name of the specification
   */
  public ScraperOwnedSpecificationTestingEntity(
      final String owner,
      final String name) {
    this(owner, name, null);
  }

  /**
   * This constructor is used to create a new instance of the class with the
   * specified {@link ScraperOwnedSpecificationReference} and a null status.
   *
   * @param reference the {@link ScraperOwnedSpecificationReference} to be used in
   *                  the test
   */
  public ScraperOwnedSpecificationTestingEntity(final ScraperOwnedSpecificationReference reference) {
    this(reference, null);
  }
}
