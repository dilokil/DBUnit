package ru.dilokill.service;

import ru.dilokill.model.Person;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class PersonService {
    private EntityManager em = Persistence
            .createEntityManagerFactory("db_lab4_2")
            .createEntityManager();

    public void save(Person person) {
        em.getTransaction().begin();
        em.persist(person);
        em.getTransaction().commit();
    }

    public void delete(Person person) {
        em.getTransaction().begin();
        em.remove(em.contains(person) ? person : em.merge(person));
        em.getTransaction().commit();
    }

    public Person get(int id) {
        return em.find(Person.class, id);
    }

    public void update(Person person) {
        em.getTransaction().begin();
        em.merge(person);
        em.getTransaction().commit();
    }

    public List<Person> getAll() {
        TypedQuery<Person> namedQuery = em.createNamedQuery("Person.getAll", Person.class);

        return namedQuery.getResultList();
    }

}
