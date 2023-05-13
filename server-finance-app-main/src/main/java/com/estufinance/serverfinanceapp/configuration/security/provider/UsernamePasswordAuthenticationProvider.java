package com.estufinance.serverfinanceapp.configuration.security.provider;

import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.estufinance.serverfinanceapp.configuration.security.service.AppUserDetailsService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

  private final PasswordEncoder passwordEncoder;
  private final AppUserDetailsService appUserDetails;
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    String username = authentication.getName();
    String password = authentication.getCredentials().toString();

    try {

      UserDetails user = appUserDetails.loadUserByUsername(username);

      if (passwordEncoder.matches(password, user.getPassword())) {

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getAuthorities().stream()
            .findFirst()
            .get().toString()));

        if (user.isEnabled()) {
            return new UsernamePasswordAuthenticationToken(username, password, authorities);

          } else {
            throw new UsernameNotFoundException("Not votacion avaliable");
          }
        } else {
          throw new UsernameNotFoundException("Account is disabled");
        }
      } catch (UsernameNotFoundException | BadCredentialsException ex) {
      throw new BadCredentialsException(ex.getMessage());
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
      return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
  }

}
