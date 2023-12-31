package com.example.stockwise.config;


import com.example.stockwise.config.filters.WarehouseFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    UserDetailsService userDetailsService;
    WarehouseFilter warehouseFilter;

    public SecurityConfiguration(UserDetailsService userDetailsService, WarehouseFilter warehouseFilter) {
        this.userDetailsService = userDetailsService;
        this.warehouseFilter = warehouseFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/v1/users/new", "/api/v1", "/api/v1/users", "/register.js", "/error").permitAll();
                    auth.requestMatchers(HttpMethod.GET,
                            "/api/v1/warehouses",
                            "/api/v1/warehouses/{id}/**",
                            "/api/rest/users/current").hasAnyAuthority("WORKER", "MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.POST,
                            "/api/v1/warehouses/{id}/orders/{taskId}/complete",
                            "/api/v1/warehouses/{id}/supplies/{taskId}/complete"
                    ).hasAnyAuthority("WORKER", "MANAGER", "ADMIN");
                    auth.requestMatchers("/api/v1/user/**").hasAnyAuthority("WORKER", "MANAGER", "ADMIN");
                    auth.requestMatchers(
                            "/api/v1/warehouses",
                            "/api/v1/warehouses/{id}/**",
                            "/api/rest/users/current").hasAnyAuthority("MANAGER", "ADMIN");
                    auth.requestMatchers("/icon.svg",
                            "/order_new.js",
                            "/register.js",
                            "/register_worker.js",
                            "/supply_new.js",
                            "/tasks.js",
                            "/user_info_edit.js",
                            "/users.js",
                            "/warehouse_id_fragment.js",
                            "/warehouses.js",
                            "/generate_form.js",
                            "/warehouse_id.js",

                            "/api/rest/warehouses/{id}").hasAnyAuthority("WORKER", "MANAGER", "ADMIN");
                    auth.requestMatchers("/error/403").hasAuthority("NONE");
                    auth.requestMatchers("/**").hasAuthority("ADMIN");
                })
                .formLogin((form -> form
                        .loginPage("/api/v1/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/api/v1", true)
                        .failureUrl("/api/v1/login?error=true")
                        .permitAll()
                ))
                .logout((logout -> logout
                        .logoutSuccessUrl("/api/v1")
                        .logoutUrl("/api/v1/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()))
                .addFilterBefore(warehouseFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
