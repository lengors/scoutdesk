package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.persistence.services.EntityService;
import io.github.lengors.scoutdesk.domain.persistence.constraints.RequireEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.Objects;

/**
 * Command for updating a scraper owned profile.
 * <p>
 * This command is used to update a {@link ScraperOwnedProfile} instance.
 *
 * @author lengors
 */
public record UpdateScraperOwnedProfileCommand() implements Command<ScraperOwnedProfile, ScraperOwnedProfile> {

  /**
   * Handler for {@link UpdateScraperOwnedProfileCommand}.
   *
   * @author lengors
   */
  @Service
  @Validated
  public static class Handler
    implements CommandHandler<UpdateScraperOwnedProfileCommand, ScraperOwnedProfile, ScraperOwnedProfile> {
    private final ScraperOwnedProfileRepository scraperOwnedProfileRepository;
    private final EntityService entityService;
    private final Handler self;

    Handler(
      final ScraperOwnedProfileRepository scraperOwnedProfileRepository,
      final EntityService entityService,
      @Lazy final Handler self
    ) {
      this.scraperOwnedProfileRepository = scraperOwnedProfileRepository;
      this.entityService = entityService;
      this.self = self;
    }

    /**
     * Handle the command.
     *
     * @param command the command
     * @param input   the input
     * @return the result
     */
    @Override
    @Transactional
    public ScraperOwnedProfile handle(
      final UpdateScraperOwnedProfileCommand command,
      final ScraperOwnedProfile input
    ) {
      return self.handle(input);
    }

    /**
     * Handle the command and performs validation.
     *
     * @param input the input
     * @return the result
     */
    protected ScraperOwnedProfile handle(final @RequireEntity(property = "name") @Valid ScraperOwnedProfile input) {
      final var entity = entityService.findEntity(input);
      if (!Objects.equals(
        entity
          .getSpecification()
          .getReference(),
        input.specification())) {
        final var specification = entityService.findEntity(input.specification());
        entity.setSpecification(specification);
      }

      entity.setInputs(Objects.requireNonNullElseGet(input.inputs(), Collections::emptyMap));
      return new ScraperOwnedProfile(scraperOwnedProfileRepository.save(entity));
    }
  }
}
