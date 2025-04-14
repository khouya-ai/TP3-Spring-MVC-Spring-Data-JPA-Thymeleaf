package khouya.site.Hopital.security.repositories;

import khouya.site.Hopital.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, String> {
    AppUser findByUsername(String username);
    AppUser findByEmail(String email);
    AppUser findByUserId(String userId);
}
