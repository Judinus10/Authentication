import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfiguration {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/login", "/register", "/forgot-password").permitAll()  // Allow access to login and register pages
                .anyRequest().authenticated()  // Any other request must be authenticated
            .and()
            .oauth2Login()
                .loginPage("/login")  // Redirect to login page if not authenticated
                .defaultSuccessUrl("/home", true);  // Redirect to home page after successful login
    }
}
