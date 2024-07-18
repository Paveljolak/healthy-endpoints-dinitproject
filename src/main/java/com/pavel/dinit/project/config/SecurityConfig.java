package com.pavel.dinit.project.config;

import com.pavel.dinit.project.components.AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity
                        .csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(registry -> {
                            registry.requestMatchers("/urls", "/register/**", "users/register").permitAll();
                            registry.requestMatchers("/urls/names").hasRole("ADMIN");
                            registry.requestMatchers("urls/{id}").hasRole("USER");
                            registry.anyRequest().authenticated();
                        })
                        .httpBasic(withDefaults());
        return httpSecurity.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails normalUser = User.builder()
//                .username("kure")
//                .password("$2a$12$uT.eMoN4MdA/gr/ekrLQYes/bUOrysgZN3GNWitw1JToceFFquQ6i")
//                .roles("USER")
//                .build();
//
//        UserDetails adminUser = User.builder()
//                .username("admin")
//                .password("$2a$12$C6C0xl5AYGcDcppabuYyV.h52bt/CQoF5qc3Rbbmr1ajJV5fX0.yu")
//                .roles("ADMIN", "USER")
//                .build();
//        return new InMemoryUserDetailsManager(normalUser, adminUser);
//    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
