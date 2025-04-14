package khouya.site.Hopital;

import khouya.site.Hopital.entities.Patient;
import khouya.site.Hopital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.Date;

@SpringBootApplication
public class HopitalApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(HopitalApplication.class, args);
    }

    @Autowired
    private PatientRepository patientRepo;

    @Override
    public void run(String... args) throws Exception {
        patientRepo.save(new Patient(null, "Ali Ali ", new Date(), true, 80));
        patientRepo.save(new Patient(null, "Fatima Fatima", new Date(), false, 95));
        patientRepo.save(new Patient(null, "Omar Omar", new Date(), true, 60));
    }

    @Bean
    CommandLineRunner commandLineRunner(JdbcUserDetailsManager jdbcUserDetailsManager){

        return args -> {
            //To add users to the database
            if (!jdbcUserDetailsManager.userExists("user1")){
                String encodedPassword = passwordEncoder().encode("1234");
                System.out.println(encodedPassword);
                jdbcUserDetailsManager.createUser(User.withUsername("user1").password(encodedPassword).roles("USER").build());
            }
            if (!jdbcUserDetailsManager.userExists("user2")){
                String encodedPassword = passwordEncoder().encode("1234");
                System.out.println(encodedPassword);
                jdbcUserDetailsManager.createUser(User.withUsername("user2").password(encodedPassword).disabled(true).roles("USER").build());
            }
            if (!jdbcUserDetailsManager.userExists("admin")){
                String encodedPassword = passwordEncoder().encode("1234");
                System.out.println(encodedPassword);
                jdbcUserDetailsManager.createUser(User.withUsername("admin").password(encodedPassword).roles("USER","ADMIN").build());
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
