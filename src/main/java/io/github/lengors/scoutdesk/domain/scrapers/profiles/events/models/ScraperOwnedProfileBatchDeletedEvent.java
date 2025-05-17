package io.github.lengors.scoutdesk.domain.scrapers.profiles.events.models;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;

/**
 * Event that is published when a batch of scraper-owned profiles is deleted.
 *
 * This event contains the list of deleted profiles as its source.
 *
 * The event is used to notify other components of the system about the deletion
 * of a batch of profiles.
 *
 * @author lengors
 */
public final class ScraperOwnedProfileBatchDeletedEvent extends ApplicationEvent {

  /**
   * Creates a new instance of the event.
   *
   * @param source The list of deleted profiles
   */
  public ScraperOwnedProfileBatchDeletedEvent(final List<ScraperOwnedProfile> source) {
    super(source);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<ScraperOwnedProfile> getSource() {
    return (List<ScraperOwnedProfile>) super.getSource();
  }
}
