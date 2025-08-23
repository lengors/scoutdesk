package io.github.lengors.scoutdesk.domain.scrapers.profiles.converters;

import io.github.lengors.scoutdesk.domain.errors.AbstractConstraintViolationHttpStatusConditionalConverter;
import io.github.lengors.scoutdesk.domain.persistence.constraints.RequireEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.SaveScraperOwnedProfileCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperNamedProfile;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
final class SaveScraperOwnedProfileCommandConstraintViolationHttpStatusConverter
  extends AbstractConstraintViolationHttpStatusConditionalConverter<SaveScraperOwnedProfileCommand.Handler> {
  SaveScraperOwnedProfileCommandConstraintViolationHttpStatusConverter() {
    super(SaveScraperOwnedProfileCommand.Handler.class);
  }

  @Override
  public HttpStatus convert(final ConstraintViolation<SaveScraperOwnedProfileCommand.Handler> source) {
    if (source.getInvalidValue() instanceof ScraperNamedProfile && RequireEntity.class.equals(source
      .getConstraintDescriptor()
      .getAnnotation()
      .annotationType())) {
      return HttpStatus.CONFLICT;
    }

    return HttpStatus.BAD_REQUEST;
  }
}
