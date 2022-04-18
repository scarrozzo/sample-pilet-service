package io.piral.feedservice.config;

import io.piral.feedservice.audit.CustomAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
        basePackages = {"io.piral.feedservice.domain"},
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*IndexRepository"),
        includeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*Repository")
)
public class PersistenceConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new CustomAuditorAware();
    }

}
