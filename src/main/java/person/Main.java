package person;

import com.github.javafaker.Faker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class Main {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example");
    private static Faker faker = new Faker();

    public static Person randomPerson() {
        Person person = new Person();

        person.setName(faker.name().name());

        Date date = faker.date().birthday();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        person.setDob(localDate);

        person.setGender(faker.options().option(Person.Gender.class));

        Address address = new Address();
        address.setCountry(faker.address().country());
        address.setState(faker.address().state());
        address.setCity(faker.address().city());
        address.setStreetAddress(faker.address().streetAddress());
        address.setZip(faker.address().zipCode());
        person.setAddress(address);

        person.setEmail(faker.internet().emailAddress());

        person.setProfession(faker.company().profession());

        return person;
    }

    public static void addPeople(int nop) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            for (int i = 0; i < nop; i++) {
                em.persist(randomPerson());
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public static List<Person> getPeople() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Person p ORDER BY p.id", Person.class).getResultList();
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        addPeople(1000);
        getPeople().forEach(System.out::println);
        emf.close();
    }
}
