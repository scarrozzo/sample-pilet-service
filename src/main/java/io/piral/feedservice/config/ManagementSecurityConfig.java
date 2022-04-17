package io.piral.feedservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Order(90)
@Configuration
public class ManagementSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String MANAGEMENT_ROLE = "MANAGEMENT";
    public static final String DEFAULT_ACTUATOR_PATH = "/actuator";
    private static final String SWAGGER_UI_PATH = "/swagger-ui";
    private static final String V_3_API_DOCS_PATH = "/v3/api-docs";

    @Value("${application.security.basic.username}")
    private String username;

    @Value("${application.security.basic.password}")
    private String password;

    @Autowired
    private Environment environment;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * Management paths
         */
        http.requestMatchers().antMatchers(DEFAULT_ACTUATOR_PATH + "/**", SWAGGER_UI_PATH + "/**", V_3_API_DOCS_PATH + "/**");

        /**
         * OpenAPI endpoints
         */
        if (environment.acceptsProfiles(Profiles.of(Profile.PROD, Profile.PRE_PROD))) {
            http.authorizeRequests().antMatchers(HttpMethod.GET, SWAGGER_UI_PATH + "/**", V_3_API_DOCS_PATH + "/**").denyAll();
        } else {
            http.authorizeRequests().antMatchers(HttpMethod.GET, SWAGGER_UI_PATH + "/**", V_3_API_DOCS_PATH + "/**").permitAll();
        }

        /**
         * Protect all management endpoints
         */
        http.authorizeRequests().anyRequest().hasRole(MANAGEMENT_ROLE);

        http.oauth2Login().disable();
        http.logout().disable();
        http.rememberMe().disable();
        http.csrf().disable();
        http.sessionManagement().disable();
        http.httpBasic();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(username)
                .password("{noop}" + password)
                .roles(MANAGEMENT_ROLE);
    }
}
