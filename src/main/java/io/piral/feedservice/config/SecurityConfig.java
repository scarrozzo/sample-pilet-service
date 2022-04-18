package io.piral.feedservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Order(100)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${application.security.basic.username}")
    private String username;

    @Value("${application.security.basic.password}")
    private String password;

    private static final String MANAGEMENT_ROLE = "MANAGEMENT";
    public static final String API_GETS = "/api/**";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * Protect pilet publish endpoint
         */
        //http.requestMatchers().antMatchers(HttpMethod.POST, PILET_PATH);

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, API_GETS).permitAll()
                .anyRequest().authenticated().and().httpBasic().and().csrf().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(username)
                .password("{noop}" + password)
                .roles(MANAGEMENT_ROLE);
    }
}
