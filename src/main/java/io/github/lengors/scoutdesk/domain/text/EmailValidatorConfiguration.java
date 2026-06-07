package io.github.lengors.scoutdesk.domain.text;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
class EmailValidatorConfiguration {
  @Bean
  EmailValidator emailValidator() {
    return EmailValidator.getInstance();
  }
}
