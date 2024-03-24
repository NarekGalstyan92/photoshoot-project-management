package am.itspace.photoshootprojectmanagement.config;

import am.itspace.photoshootprojectmanagement.entity.Role;
import am.itspace.photoshootprojectmanagement.security.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        //Login and Register
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/admin/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/login").permitAll()
                        .requestMatchers("/users/loginPage").permitAll()
                        .requestMatchers("/users/loginSuccess").authenticated()
                        .requestMatchers("/users/register").permitAll()
                        // Reviews
                        .requestMatchers("/reviews").permitAll()
                        .requestMatchers("/reviews/create").hasAnyAuthority(Role.USER.name())
                        .requestMatchers("/reviews/update/**").hasAnyAuthority(Role.USER.name())
                        // Bookings
                        .requestMatchers("/bookings").permitAll()
                        .requestMatchers("/bookings/create/**").authenticated()
                        .requestMatchers("/bookings/update/**").authenticated()
                        // Event Categories
                        .requestMatchers(HttpMethod.GET, "/eventCategories").permitAll()
                        .requestMatchers("/eventCategories/**").hasAnyAuthority(Role.ADMIN.name())
                        .anyRequest().authenticated()

                )
                .formLogin(formLogin -> formLogin
                        .loginProcessingUrl("/login")
                        .loginPage("/users/loginPage")
                        .defaultSuccessUrl("/", true)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                )
                .headers(headers -> headers.xssProtection(
                        xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
                ));

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }
}