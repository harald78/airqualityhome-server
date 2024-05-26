package life.airqualityhome.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /*@Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                //.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                        .requestMatchers("/api/**")
                        .authenticated()
                        .requestMatchers("/v3/**")
                        .permitAll()
                )
                .httpBasic(withDefaults());
        return http.build();
    }*/

    /*@Bean
    public UserDetailsService userDetailsService() {
        var user = User.withUsername("user")
                .password("{noop}s3cret!")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }*/

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(request -> request.requestMatchers(new AntPathRequestMatcher("/api/**"))
                .authenticated())
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(request -> request.requestMatchers(new AntPathRequestMatcher("/swagger-ui**"))
                .permitAll())
                .build();
    }
}
