package io.github.lengors.scoutdesk.domain.scrapers.profiles.converters;

import io.github.lengors.scoutdesk.domain.persistence.constraints.RequireEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.UpdateScraperOwnedProfileCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperNamedProfile;
import io.github.lengors.scoutdesk.domain.spring.core.converters.AbstractConstraintViolationHttpStatusCodeConditionalConverter;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
class UpdateScraperOwnedProfileCommandConstraintViolationHttpStatusCodeConverter extends
  AbstractConstraintViolationHttpStatusCodeConditionalConverter<UpdateScraperOwnedProfileCommand.Handler> {
  UpdateScraperOwnedProfileCommandConstraintViolationHttpStatusCodeConverter() {
    super(UpdateScraperOwnedProfileCommand.Handler.class);
  }

  @Override
  public HttpStatus convert(final @NotNull ConstraintViolation<UpdateScraperOwnedProfileCommand.Handler> source) {
    if (source.getInvalidValue() instanceof ScraperNamedProfile && RequireEntity.class.equals(source
      .getConstraintDescriptor()
      .getAnnotation()
      .annotationType())) {
      return HttpStatus.NOT_FOUND;
    }

    return HttpStatus.BAD_REQUEST;
  }
}
