package io.github.lengors.scoutdesk.domain.persistence.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.ResolvableType;

/**
 * Represents a reference to an entity of type E.
 *
 * @param <E> The type of the entity being referred to.
 * @author lengors
 */
public interface EntityReferrer<E extends @NotNull Object> {
  /**
   * Gets the type name of the entity being referred to.
   *
   * @return The type name of the entity.
   */
  @JsonIgnore
  default String getTypeName() {
    return ResolvableType
      .forInstance(this)
      .getGeneric(0)
      .getType()
      .getTypeName();
  }

  /**
   * Indicates whether the referred entity is an individual entity.
   * <p>
   * By default, this method returns true, indicating that the entity is individual. Override this method if the entity
   * is not individual.
   *
   * @return true if the entity is individual, false otherwise.
   */
  @JsonIgnore
  default boolean isIndividualEntity() {
    return true;
  }
}
