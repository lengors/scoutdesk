package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.persistence.services.EntityService;
import io.github.lengors.scoutdesk.domain.persistence.constraints.RequireEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileReference;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.Objects;

/**
 * Command for saving a scraper owned profile.
 * <p>
 * This command is used to save a {@link ScraperOwnedProfile} instance.
 *
 * @author lengors
 */
public record SaveScraperOwnedProfileCommand() implements Command<ScraperOwnedProfile, ScraperOwnedProfile> {

  /**
   * Handler for {@link SaveScraperOwnedProfileCommand}.
   *
   * @author lengors
   */
  @Service
  @Validated
  public static class Handler
    implements CommandHandler<SaveScraperOwnedProfileCommand, ScraperOwnedProfile, ScraperOwnedProfile> {
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
      final SaveScraperOwnedProfileCommand command,
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
    protected ScraperOwnedProfile handle(
      final @RequireEntity(absent = true, property = "name") @Valid ScraperOwnedProfile input
    ) {
      final var reference = new ScraperOwnedProfileReference(input.owner(), input.name());
      final var specification = entityService.findEntity(input.specification());
      final var inputs = Objects.requireNonNullElseGet(input.inputs(), Collections::<String, String>emptyMap);
      final var entity = new ScraperOwnedProfileEntity(reference, inputs, specification);
      return new ScraperOwnedProfile(scraperOwnedProfileRepository.save(entity));
    }
  }
}
