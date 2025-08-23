package io.github.lengors.scoutdesk.domain.scrapers.profiles.converters;

import io.github.lengors.scoutdesk.domain.errors.AbstractConstraintViolationHttpStatusConditionalConverter;
import io.github.lengors.scoutdesk.domain.persistence.constraints.RequireEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.UpdateScraperOwnedProfileCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperNamedProfile;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
final class UpdateScraperOwnedProfileCommandConstraintViolationHttpStatusConverter
  extends AbstractConstraintViolationHttpStatusConditionalConverter<UpdateScraperOwnedProfileCommand.Handler> {
  UpdateScraperOwnedProfileCommandConstraintViolationHttpStatusConverter() {
    super(UpdateScraperOwnedProfileCommand.Handler.class);
  }

  @Override
  public HttpStatus convert(final ConstraintViolation<UpdateScraperOwnedProfileCommand.Handler> source) {
    if (source.getInvalidValue() instanceof ScraperNamedProfile && RequireEntity.class.equals(source
      .getConstraintDescriptor()
      .getAnnotation()
      .annotationType())) {
      return HttpStatus.NOT_FOUND;
    }

    return HttpStatus.BAD_REQUEST;
  }
}
