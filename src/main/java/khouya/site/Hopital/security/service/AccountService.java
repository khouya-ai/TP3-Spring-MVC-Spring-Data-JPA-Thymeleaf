package khouya.site.Hopital.security.service;


import khouya.site.Hopital.security.entities.AppRole;
import khouya.site.Hopital.security.entities.AppUser;

public interface AccountService {
    AppUser addNewUser(String username, String password, String email, String confirmPassword);
    AppRole addNewRole(String role);
    void addRoleToUser(String username, String role);
    void removeRoleFromUser(String username, String role);
    AppUser loadUserByUsername(String username);
    AppRole loadRole(String role);

}
