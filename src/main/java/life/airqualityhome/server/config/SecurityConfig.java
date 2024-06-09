package life.airqualityhome.server.config;

import life.airqualityhome.server.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    private final SensorApiTokenFilter apiTokenFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, SensorApiTokenFilter apiTokenFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.apiTokenFilter = apiTokenFilter;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsServiceImpl();
    }


    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    @Order(1)
    @Profile({"prod", "local"})
    public SecurityFilterChain sensorSecurityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests((exchange) -> exchange
                        .requestMatchers(mvc.pattern("/swagger-resources"),
                                mvc.pattern("/swagger-resources/**"),
                                mvc.pattern("/webjars/**"),
                                mvc.pattern("/v3/api-docs/**"),
                                mvc.pattern("/swagger-ui/**"),
                                mvc.pattern( "/swagger-ui.html"))
                        .permitAll())
                .authorizeHttpRequests((exchange) ->
                        exchange.requestMatchers("/api/app/user/save", "/api/app/user/login", "/api/app/user/logout",
                                "/api/app/user/refreshToken").permitAll())
                .authorizeHttpRequests((exchange) ->
                        exchange.requestMatchers("/api/sensor/**").authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(apiTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((exchange) ->
                        exchange.requestMatchers("/api/app/**").authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    @Order(2)
    @Profile({"test"})
    public SecurityFilterChain testSecurityFilter(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests((exchange) ->
                        exchange.anyRequest().permitAll())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(apiTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
