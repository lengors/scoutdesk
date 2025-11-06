package io.github.lengors.scoutdesk.domain.scrapers.commands.converters;

import io.github.lengors.scoutdesk.domain.persistence.constraints.RequireEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.SaveScraperOwnedProfileCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperNamedProfile;
import io.github.lengors.scoutdesk.domain.spring.core.converters.AbstractConstraintViolationHttpStatusCodeConditionalConverter;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
class SaveScraperOwnedProfileCommandConstraintViolationHttpStatusCodeConverter
  extends AbstractConstraintViolationHttpStatusCodeConditionalConverter<SaveScraperOwnedProfileCommand.Handler> {
  SaveScraperOwnedProfileCommandConstraintViolationHttpStatusCodeConverter() {
    super(SaveScraperOwnedProfileCommand.Handler.class);
  }

  @Override
  public HttpStatus convert(final @NotNull ConstraintViolation<SaveScraperOwnedProfileCommand.Handler> source) {
    if (source.getInvalidValue() instanceof ScraperNamedProfile && RequireEntity.class.equals(source
      .getConstraintDescriptor()
      .getAnnotation()
      .annotationType())) {
      return HttpStatus.CONFLICT;
    }

    return HttpStatus.BAD_REQUEST;
  }
}
