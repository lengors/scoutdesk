package io.github.lengors.scoutdesk.domain.scrapers.specifications.constraints;

import io.github.lengors.scoutdesk.domain.scrapers.profiles.constraints.ScraperNamelessProfileSpecificationCompliantValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validation annotation to ensure that an object holding a specification complies with it.
 *
 * @author lengors
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ScraperNamelessProfileSpecificationCompliantValidator.class)
public @interface ScraperSpecificationCompliant {

  /**
   * The default error message to be returned if the validation fails.
   *
   * @return the default error message
   */
  String message() default "%s";

  /**
   * Allows the specification of validation groups, to which this constraint belongs.
   *
   * @return the validation groups
   */
  Class<?>[] groups() default {};

  /**
   * Can be used by clients of the Bean Validation API to assign custom payload objects to a constraint.
   *
   * @return the custom payload objects
   */
  Class<? extends Annotation>[] payload() default {};
}
