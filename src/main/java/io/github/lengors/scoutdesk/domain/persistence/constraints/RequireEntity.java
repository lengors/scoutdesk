package io.github.lengors.scoutdesk.domain.persistence.constraints;

import io.github.lengors.scoutdesk.domain.persistence.services.EntityReferrer;
import jakarta.validation.Constraint;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated entity must be present or absent in the database.
 * <p>
 * The annotation can be applied to fields, parameters, or types. It uses the {@link RequireEntityValidator} to perform
 * the validation.
 *
 * @author lengors
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
@Constraint(validatedBy = RequireEntityValidator.class)
public @interface RequireEntity {

  /**
   * If true, the annotated entity must be absent (i.e., not exist). If false, the annotated entity must be present.
   *
   * @return true if the entity must be absent, false if it must be present
   */
  @AliasFor("absent")
  boolean value() default false;

  /**
   * The error message template. It can include a single '%s' placeholder, which will be replaced with the string
   * representation of the invalid value in the validator.
   *
   * @return the error message template
   */
  String message() default "%s";

  /**
   * Allows the specification of validation groups, to which this constraint belongs.
   *
   * @return the groups
   */
  Class<?>[] groups() default {};

  /**
   * Payload for clients of the Bean Validation API to specify additional information about the validation failure.
   *
   * @return the payload classes
   */
  Class<? extends Annotation>[] payload() default {};

  /**
   * If true, the annotated entity must be absent (i.e., not exist). If false, the annotated entity must be present.
   *
   * @return true if the entity must be absent, false if it must be present
   */
  @AliasFor("value")
  boolean absent() default false;

  /**
   * The property path to which the validation error should be attached.
   *
   * @return the property path
   */
  String property() default StringUtils.EMPTY;

  /**
   * The type of the entity referrer.
   *
   * @return the class of the entity referrer
   */
  Class<? extends EntityReferrer> referrerType() default EntityReferrer.class;
}
