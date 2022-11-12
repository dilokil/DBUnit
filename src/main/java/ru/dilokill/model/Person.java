package ru.dilokill.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "person")
@NamedQuery(name = "Person.getAll", query = "select p from Person p")
public class Person {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String surname;

    private BigDecimal money;

    public Person() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}