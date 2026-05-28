package com.unimag.unimagleisureinventory.config;

import com.unimag.unimagleisureinventory.model.checkout.CheckOut;
import com.unimag.unimagleisureinventory.model.enums.*;
import com.unimag.unimagleisureinventory.model.item.Item;
import com.unimag.unimagleisureinventory.model.item.ItemType;
import com.unimag.unimagleisureinventory.model.penalty.Penalty;
import com.unimag.unimagleisureinventory.model.penalty.PenaltyType;
import com.unimag.unimagleisureinventory.model.person.Person;
import com.unimag.unimagleisureinventory.model.person.Student;
import com.unimag.unimagleisureinventory.model.reservation.Reservation;
import com.unimag.unimagleisureinventory.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final PersonRepository personRepository;
    private final StudentRepository studentRepository;
    private final PenaltyTypeRepository penaltyTypeRepository;
    private final ItemTypeRepository itemTypeRepository;
    private final ItemRepository itemRepository;
    private final ReservationRepository reservationRepository;
    private final CheckOutRepository checkOutRepository;
    private final PenaltyRepository penaltyRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (personRepository.count() > 0) {
            log.info("Base de datos ya tiene datos, omitiendo seed...");
            return;
        }

        log.info("Cargando datos de prueba...");

        List<PenaltyType> penaltyTypes = createPenaltyTypes();
        List<ItemType>    itemTypes    = createItemTypes();
        List<Item>        items        = createItems(itemTypes);
        Person            admin        = createAdmin();
        Person            clerk        = createClerk();
        List<Student>     students     = createStudents();

        createReservationsAndCheckouts(students, items, penaltyTypes);

        log.info("Seed completado — admin: admin@unimagdalena.edu.co / admin123");
        log.info("Auxiliar: auxiliar@unimagdalena.edu.co / clerk123");
        log.info("Estudiantes: estudiante1..3@unimagdalena.edu.co / student123");
    }

    // ── Penalty types ──────────────────────────────────
    private List<PenaltyType> createPenaltyTypes() {
        PenaltyType overdue  = new PenaltyType(); overdue.setName("OVERDUE");
        PenaltyType damaged  = new PenaltyType(); damaged.setName("DAMAGED_ITEM");
        return penaltyTypeRepository.saveAll(List.of(overdue, damaged));
    }

    // ── Item types ─────────────────────────────────────
    private List<ItemType> createItemTypes() {
        for (String name : List.of("Deportes", "Juegos de mesa", "Audiovisual", "Recreación")) {
            ItemType t = new ItemType();
            t.setName(name);
            itemTypeRepository.save(t);
        }
        return itemTypeRepository.findAll();
    }

    // ── Items ──────────────────────────────────────────
    private List<Item> createItems(List<ItemType> types) {
        ItemType deportes   = types.get(0);
        ItemType juegos     = types.get(1);
        ItemType audiovisual = types.get(2);
        ItemType recreacion = types.get(3);

        return itemRepository.saveAll(List.of(
                buildItem("Balón de fútbol",      "Balón #5 oficial",            5, 5, deportes,    ItemStatus.AVAILABLE,    ItemCondition.GOOD),
                buildItem("Balón de baloncesto",  "Balón NBA réplica",           3, 3, deportes,    ItemStatus.AVAILABLE,    ItemCondition.GOOD),
                buildItem("Raquetas de tenis",    "Par de raquetas con funda",   4, 4, deportes,    ItemStatus.AVAILABLE,    ItemCondition.NEW),
                buildItem("Ajedrez",              "Tablero y piezas completos",  6, 6, juegos,      ItemStatus.AVAILABLE,    ItemCondition.GOOD),
                buildItem("Monopolio",            "Edición clásica",             3, 3, juegos,      ItemStatus.AVAILABLE,    ItemCondition.WORN),
                buildItem("Dominó",               "Fichas doble 6",              8, 8, juegos,      ItemStatus.AVAILABLE,    ItemCondition.GOOD),
                buildItem("Cámara fotográfica",   "Canon EOS Rebel SL3",         2, 2, audiovisual, ItemStatus.AVAILABLE,    ItemCondition.GOOD),
                buildItem("Proyector",            "Epson 3500 lúmenes",          1, 1, audiovisual, ItemStatus.AVAILABLE,    ItemCondition.GOOD),
                buildItem("Juego de frisbee",     "Set de 3 discos",             5, 5, recreacion,  ItemStatus.AVAILABLE,    ItemCondition.GOOD),
                buildItem("Cuerda de saltar",     "Cuerda grupal 5m",            4, 4, recreacion,  ItemStatus.AVAILABLE,    ItemCondition.NEW)
        ));
    }

    private Item buildItem(String name, String desc, int total, int available,
                           ItemType type, ItemStatus status, ItemCondition condition) {
        Item i = new Item();
        i.setName(name);
        i.setDescription(desc);
        i.setTotalQuantity(total);
        i.setAvailableQuantity(available);
        i.setItemType(type);
        i.setItemStatus(status);
        i.setItemCondition(condition);
        return i;
    }

    // ── Admin ──────────────────────────────────────────
    private Person createAdmin() {
        Person admin = new Person();
        admin.setFirstName("Admin");
        admin.setLastName("Bienestar");
        admin.setEmail("admin@unimagdalena.edu.co");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);
        return personRepository.save(admin);
    }

    // ── Clerk ──────────────────────────────────────────
    private Person createClerk() {
        Person clerk = new Person();
        clerk.setFirstName("Carlos");
        clerk.setLastName("Auxiliar");
        clerk.setEmail("auxiliar@unimagdalena.edu.co");
        clerk.setPassword(passwordEncoder.encode("clerk123"));
        clerk.setRole(Role.INVENTORY_CLERK);
        return personRepository.save(clerk);
    }

    // ── Students ───────────────────────────────────────
    private List<Student> createStudents() {
        return List.of(
                buildStudent(2022114001L, "Laura",   "Martínez", "estudiante1@unimagdalena.edu.co"),
                buildStudent(2022114002L, "Andrés",  "Pérez",    "estudiante2@unimagdalena.edu.co"),
                buildStudent(2022114003L, "Valentina","García",  "estudiante3@unimagdalena.edu.co")
        );
    }

    private Student buildStudent(Long id, String firstName, String lastName, String email) {
        Person p = new Person();
        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setEmail(email);
        p.setPassword(passwordEncoder.encode("student123"));
        p.setRole(Role.STUDENT);
        personRepository.save(p);

        Student s = new Student();
        s.setStudentId(id);
        s.setPerson(p);
        return studentRepository.save(s);
    }

    // ── Reservations, checkouts y penalties ────────────
    private void createReservationsAndCheckouts(
            List<Student> students, List<Item> items, List<PenaltyType> penaltyTypes) {

        Student laura    = students.get(0);
        Student andres   = students.get(1);
        Student valentina = students.get(2);

        Item balon    = items.get(0);
        Item ajedrez  = items.get(3);
        Item camara   = items.get(6);
        Item frisbee  = items.get(8);

        PenaltyType overdue  = penaltyTypes.get(0);
        PenaltyType damaged  = penaltyTypes.get(1);

        // Laura — préstamo activo (en curso)
        Reservation r1 = buildReservation(laura, balon, ReservationStatus.ACCEPTED,
                LocalDateTime.now().minusHours(3),
                LocalDateTime.now().minusHours(2));
        reservationRepository.save(r1);

        CheckOut c1 = new CheckOut();
        c1.setReservation(r1);
        c1.setStudent(laura);
        c1.setCheckOutDate(LocalDateTime.now().minusHours(2));
        c1.setDueDate(LocalDateTime.now().plusHours(2));
        c1.setStatus(CheckOutStatus.ACTIVE);
        checkOutRepository.save(c1);
        balon.setAvailableQuantity(balon.getAvailableQuantity() - 1);
        itemRepository.save(balon);

        // Andrés — préstamo vencido + sanción activa
        Reservation r2 = buildReservation(andres, camara, ReservationStatus.ACCEPTED,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(2).plusMinutes(30));
        reservationRepository.save(r2);

        CheckOut c2 = new CheckOut();
        c2.setReservation(r2);
        c2.setStudent(andres);
        c2.setCheckOutDate(LocalDateTime.now().minusDays(2));
        c2.setDueDate(LocalDateTime.now().minusDays(1));
        c2.setStatus(CheckOutStatus.EXPIRED);
        checkOutRepository.save(c2);
        camara.setAvailableQuantity(camara.getAvailableQuantity() - 1);
        itemRepository.save(camara);

        Penalty p1 = new Penalty();
        p1.setStudent(andres);
        p1.setCheckOut(c2);
        p1.setPenaltyType(overdue);
        p1.setReason("Item no devuelto a tiempo. Vencimiento: " + c2.getDueDate());
        p1.setStartDate(LocalDateTime.now().minusDays(1));
        p1.setPenaltyStatus(PenaltyStatus.ACTIVE);
        penaltyRepository.save(p1);

        // Valentina — préstamo devuelto con daño + sanción resuelta
        Reservation r3 = buildReservation(valentina, ajedrez, ReservationStatus.ACCEPTED,
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(5).plusMinutes(30));
        reservationRepository.save(r3);

        CheckOut c3 = new CheckOut();
        c3.setReservation(r3);
        c3.setStudent(valentina);
        c3.setCheckOutDate(LocalDateTime.now().minusDays(5));
        c3.setDueDate(LocalDateTime.now().minusDays(4));
        c3.setCheckInDate(LocalDateTime.now().minusDays(4));
        c3.setReturnedItemCondition(ItemCondition.DAMAGED);
        c3.setStatus(CheckOutStatus.RETURNED);
        checkOutRepository.save(c3);

        Penalty p2 = new Penalty();
        p2.setStudent(valentina);
        p2.setCheckOut(c3);
        p2.setPenaltyType(damaged);
        p2.setReason("Artículo devuelto en mal estado");
        p2.setStartDate(LocalDateTime.now().minusDays(4));
        p2.setEndDate(LocalDateTime.now().minusDays(2));
        p2.setPenaltyStatus(PenaltyStatus.RESOLVED);
        penaltyRepository.save(p2);

        // Laura — reserva pendiente para frisbee
        Reservation r4 = buildReservation(laura, frisbee, ReservationStatus.PENDING,
                LocalDateTime.now().plusMinutes(10),
                LocalDateTime.now().plusMinutes(40));
        reservationRepository.save(r4);
    }

    private Reservation buildReservation(Student student, Item item,
                                         ReservationStatus status,
                                         LocalDateTime reservationTime,
                                         LocalDateTime claimDeadline) {
        Reservation r = new Reservation();
        r.setStudent(student);
        r.setItem(item);
        r.setStatus(status);
        r.setReservationTime(reservationTime);
        r.setClaimDeadline(claimDeadline);
        return r;
    }
}