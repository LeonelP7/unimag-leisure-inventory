package com.unimag.unimagleisureinventory.config;

import com.unimag.unimagleisureinventory.model.enums.Role;
import com.unimag.unimagleisureinventory.model.penalty.PenaltyType;
import com.unimag.unimagleisureinventory.model.person.Person;
import com.unimag.unimagleisureinventory.repositories.PenaltyTypeRepository;
import com.unimag.unimagleisureinventory.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final PersonRepository personRepository;
    private final PenaltyTypeRepository penaltyTypeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        createDefaultAdmin();
        createDefaultPenaltyTypes();
    }

    private void createDefaultAdmin() {
        if (personRepository.existsByEmail("admin@unimagdalena.edu.co")) {
            log.info("Admin already exists, skipping...");
            return;
        }

        Person admin = new Person();
        admin.setFirstName("Admin");
        admin.setLastName("Unimag");
        admin.setEmail("admin@unimagdalena.edu.co");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);

        personRepository.save(admin);
        log.info("Default admin created: admin@unimagdalena.edu.co / admin123");
    }

    private void createDefaultPenaltyTypes() {
        createPenaltyTypeIfNotExists("OVERDUE");
        createPenaltyTypeIfNotExists("DAMAGED_ITEM");
    }

    private void createPenaltyTypeIfNotExists(String name) {
        if (penaltyTypeRepository.existsByName(name)) {
            log.info("Penalty type '{}' already exists, skipping...", name);
            return;
        }

        PenaltyType penaltyType = new PenaltyType();
        penaltyType.setName(name);
        penaltyTypeRepository.save(penaltyType);
        log.info("Penalty type '{}' created", name);
    }
}
