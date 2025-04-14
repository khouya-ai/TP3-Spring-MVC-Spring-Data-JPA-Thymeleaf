package khouya.site.Hopital.security.service.Impl;

import khouya.site.Hopital.security.entities.AppRole;
import khouya.site.Hopital.security.service.AccountService;
import lombok.AllArgsConstructor;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var appUser = accountService.loadUserByUsername(username);
        if (appUser == null) {
            throw new UsernameNotFoundException(String.format("User %s not found", username));
        }
        return User
                .withUsername(appUser.getUsername())
                .password(appUser.getPassword())
                .roles(
                        appUser.getRoles().stream().map(
                                AppRole::getRole
                        ).toArray(String[]::new)
                )
                .build();
    }
}
