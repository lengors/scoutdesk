package io.github.lengors.scoutdesk.domain.spring.security.services;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * Configures the {@link ProxiedAuthenticationImpersonationFilter} to be added before the {@link AuthenticationFilter}
 * in the security filter chain. This filter is used to handle impersonation of proxied authentication requests.
 * <p>
 * It is only added if the {@link ProxiedAuthenticationImpersonationFilter} bean is present in the application context.
 * If the filter is not present, it will not be added to the security filter chain.
 * <p>
 * This class extends {@link SecurityConfigurerAdapter} to allow for custom configuration of the security filter chain.
 * It is annotated with {@link Component} to make it a Spring-managed bean.
 * <p>
 * The filter is added before the {@link AuthenticationFilter} to ensure that it processes requests before the
 * authentication process begins. This is important for scenarios where the filter needs to modify the request or
 * perform actions related to impersonation before the authentication filter processes the request.
 *
 * @author lengors
 */
@Component
public final class ProxiedAuthenticationImpersonationFilterConfigurerAdapter
  extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
  private final @Nullable ProxiedAuthenticationImpersonationFilter proxiedAuthenticationImpersonationFilter;

  /**
   * Creates a new instance of {@link ProxiedAuthenticationImpersonationFilterConfigurerAdapter}.
   * <p>
   * This constructor is used by Spring to inject the {@link ProxiedAuthenticationImpersonationFilter} bean if it is
   * present in the application context. If the filter is not present, it will be null, and the filter will not be added
   * to the security filter chain.
   * <p>
   * The filter is added to the security filter chain in the {@link #configure(HttpSecurity)} method, which is called
   * during the security configuration phase.
   *
   * @param proxiedAuthenticationImpersonationFilter the filter to be added to the security filter chain, or null if not
   *                                                 present
   */
  public ProxiedAuthenticationImpersonationFilterConfigurerAdapter(
    @Autowired(required = false)
    final @Nullable ProxiedAuthenticationImpersonationFilter proxiedAuthenticationImpersonationFilter
  ) {
    this.proxiedAuthenticationImpersonationFilter = proxiedAuthenticationImpersonationFilter;
  }

  @Override
  public void configure(final HttpSecurity builder) throws Exception {
    super.configure(builder);
    if (proxiedAuthenticationImpersonationFilter != null) {
      builder.addFilterBefore(proxiedAuthenticationImpersonationFilter, AuthenticationFilter.class);
    }
  }
}
