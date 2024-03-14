package com.epam.gymapp.repository;

import com.epam.gymapp.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository implements GeneralRepository<Long, User> {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public User save(User user) {
    entityManager.persist(user);
    return user;
  }

  @Override
  public List<User> findAll() {
    return entityManager.createQuery("select u from User u", User.class)
        .getResultList();
  }

  public List<User> findAllByFirstAndLastNames(String firstName, String lastName) {
    String usernamePrefix = String.format("%s.%s", firstName, lastName);

    return entityManager.createQuery("""
            select u from User u
            where u.username like :username
            """, User.class)
        .setParameter("username", usernamePrefix + "%")
        .getResultList();
  }

  @Override
  public Optional<User> findById(Long id) {
    return entityManager.createQuery("""
            select u from User u
            where u.id = :id
            """, User.class)
        .setParameter("id", id)
        .getResultStream()
        .findFirst();
  }

  public Optional<User> findByUsername(String username) {
    return entityManager.createQuery("""
            select u from User u
            where lower(u.username)=lower(:username)
            """, User.class)
        .setParameter("username", username)
        .getResultStream()
        .findFirst();
  }

  @Override
  public User update(User user) {
    return entityManager.merge(user);
  }

  @Override
  public void delete(User user) {
    entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
  }
}
