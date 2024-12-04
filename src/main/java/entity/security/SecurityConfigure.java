package entity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfigure {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf((csrf) -> csrf.disable());

        http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                .requestMatchers("/login", "/register").permitAll()
                .anyRequest().authenticated());

        http.formLogin(log -> log
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true));

//        http.formLogin(withDefaults());
        http.logout(withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}123")
                .roles("ADMIN")
                .build();

        UserDetails ali = User.builder()
                .username("ali")
                .password("{noop}123")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, ali);
    }

    /*@Bean
    public PasswordEncoder passwordEncoder() {
        return new NoOpPasswordEncoder().getInstance();
    }*/
}
