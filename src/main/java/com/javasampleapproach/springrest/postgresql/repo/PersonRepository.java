package com.javasampleapproach.springrest.postgresql.repo;

import com.javasampleapproach.springrest.postgresql.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByEmail(String email);
}
