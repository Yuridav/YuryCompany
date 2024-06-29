package org.MyProject.Jpa.Config;

import org.MyProject.Jpa.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;

import java.security.Security;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    CustomerService customerService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(request ->
                                new CorsConfiguration().applyPermitDefaultValues()))
                .securityContext(securityContext-> securityContext.securityContextRepository(new HttpSessionSecurityContextRepository()))
                .sessionManagement(session -> session.maximumSessions(1))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/auth/login1").permitAll()
                        .requestMatchers("/auth/reg").permitAll()
                        .requestMatchers("/auth/getName").fullyAuthenticated()
                        .requestMatchers("/admin/*").hasAuthority("ADMIN")
                        .anyRequest().fullyAuthenticated())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder bCryptPasswordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);

        providerManager.setEraseCredentialsAfterAuthentication(false);
        System.out.println("Dao work");
        return providerManager;
    }



}
