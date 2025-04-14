package khouya.site.Hopital.security.service.Impl;

import khouya.site.Hopital.security.entities.AppRole;
import khouya.site.Hopital.security.entities.AppUser;
import khouya.site.Hopital.security.repositories.AppRoleRepository;
import khouya.site.Hopital.security.repositories.AppUserRepository;
import khouya.site.Hopital.security.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private AppUserRepository userRepository;
    private AppRoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public AppUser addNewUser(String username, String password, String email, String confirmPassword) {
        if (userRepository.findByUsername(username) != null) {
            throw new RuntimeException("User already exists");
        }

        if (!password.equals(confirmPassword)) {
            throw new RuntimeException("Password and confirmation do not match");
        }

        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("Email already exists");
        }

        if (username == null || password == null || email == null) {
            throw new RuntimeException("Username, password, and email cannot be null");
        }

        AppUser appUser = AppUser.builder()
                .userId(UUID.randomUUID().toString())
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();

        return userRepository.save(appUser);

    }

    @Override
    public AppRole addNewRole(String role) {
        if (roleRepository.findByRole(role) != null) {
            throw new RuntimeException("Role already exists");
        }
        if (role == null) {
            throw new RuntimeException("Role cannot be null");
        }
        AppRole appRole = AppRole.builder()
                .role(role)
                .build();
        return roleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String role) {
        AppUser appUser = userRepository.findByUsername(username);
        if (appUser == null) {
            throw new RuntimeException("User not found");
        }
        AppRole appRole = roleRepository.findByRole(role);
        if (appRole == null) {
            throw new RuntimeException("Role not found");
        }
        appUser.getRoles().add(appRole);

    }

    @Override
    public void removeRoleFromUser(String username, String role) {
        AppUser appUser = userRepository.findByUsername(username);
        if (appUser == null) {
            throw new RuntimeException("User not found");
        }
        AppRole appRole = roleRepository.findByRole(role);
        if (appRole == null) {
            throw new RuntimeException("Role not found");
        }
        appUser.getRoles().remove(appRole);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public AppRole loadRole(String role) {
        return roleRepository.findByRole(role);
    }


}
