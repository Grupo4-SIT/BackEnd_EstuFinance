package com.estufinance.serverfinanceapp.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.estufinance.serverfinanceapp.configuration.security.provider.UsernamePasswordAuthenticationProvider;
import com.estufinance.serverfinanceapp.configuration.security.service.AppUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {
    
    private final AppUserDetailsService appUserDetailsService;
    private final UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;

    @Bean
    public UserDetailsService userDetailsService() {
      return appUserDetailsService;
    }
  
    @Bean
    public AuthenticationProvider authenticationProvider() {
      return usernamePasswordAuthenticationProvider;
    }
  
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
      return configuration.getAuthenticationManager();
    }

}
