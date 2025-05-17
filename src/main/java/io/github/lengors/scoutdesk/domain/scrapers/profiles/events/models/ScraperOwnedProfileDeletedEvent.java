package io.github.lengors.scoutdesk.domain.scrapers.profiles.events.models;

import org.springframework.context.ApplicationEvent;

import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;

/**
 * Event that is published when a scraper-owned profile is deleted.
 *
 * This event contains the deleted profile as its source.
 *
 * The event is used to notify other components of the system about the deletion
 * of a profile.
 *
 * @author lengors
 */
public final class ScraperOwnedProfileDeletedEvent extends ApplicationEvent {

  /**
   * Creates a new instance of the event.
   *
   * @param source The deleted profile
   */
  public ScraperOwnedProfileDeletedEvent(final ScraperOwnedProfile source) {
    super(source);
  }

  @Override
  public ScraperOwnedProfile getSource() {
    return (ScraperOwnedProfile) super.getSource();
  }
}
