package pn.nutrimeter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import pn.nutrimeter.service.services.api.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    public SpringSecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                    .disable()
                .csrf()
//                    .csrfTokenRepository(this.csrfTokenRepository())
                    .disable()
//                .and()
                .authorizeRequests()
                    .antMatchers("/favicon.ico", "/js/*", "/css/*", "/img/*").permitAll()
                    .antMatchers("/", "/register", "/login").anonymous()
                .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/home")
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                .and()
                    .rememberMe()
                        .rememberMeParameter("rememberMe")
                        .key("rmmbrm")
                        .userDetailsService(this.userService)
                        .rememberMeCookieName("RMMBRM")
                        .tokenValiditySeconds(1200);
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");

        return repository;
    }
}
