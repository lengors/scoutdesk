package io.github.lengors.scoutdesk.domain.scrapers.specifications.converters;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.persistence.constraints.RequireEntity;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.SaveScraperOwnedSpecificationCommand;
import io.github.lengors.scoutdesk.domain.spring.core.converters.AbstractConstraintViolationHttpStatusCodeConditionalConverter;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

@Component
class SaveScraperOwnedSpecificationCommandConstraintViolationHttpStatusCodeConverter extends
  AbstractConstraintViolationHttpStatusCodeConditionalConverter<SaveScraperOwnedSpecificationCommand.Handler> {
  SaveScraperOwnedSpecificationCommandConstraintViolationHttpStatusCodeConverter() {
    super(SaveScraperOwnedSpecificationCommand.Handler.class);
  }

  @Override
  public HttpStatusCode convert(
    final @NotNull ConstraintViolation<SaveScraperOwnedSpecificationCommand.Handler> source
  ) {
    if (source.getInvalidValue() instanceof ScraperSpecification && RequireEntity.class.equals(source
      .getConstraintDescriptor()
      .getAnnotation()
      .annotationType())) {
      return HttpStatus.CONFLICT;
    }

    return HttpStatus.BAD_REQUEST;
  }
}
