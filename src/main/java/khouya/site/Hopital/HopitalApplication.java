package khouya.site.Hopital;

import khouya.site.Hopital.entities.Patient;
import khouya.site.Hopital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
}
