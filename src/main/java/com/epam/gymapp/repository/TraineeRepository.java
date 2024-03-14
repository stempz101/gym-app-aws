package com.epam.gymapp.repository;

import com.epam.gymapp.model.Trainee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeRepository implements GeneralRepository<Long, Trainee> {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Trainee save(Trainee trainee) {
    entityManager.persist(trainee);
    return trainee;
  }

  @Override
  public List<Trainee> findAll() {
    return entityManager.createQuery("""
            select t from Trainee t
            join fetch t.user
            left join fetch t.trainers tr
            left join fetch tr.user
            left join fetch tr.specialization
            """, Trainee.class)
        .getResultList();
  }

  @Override
  public Optional<Trainee> findById(Long id) {
    return entityManager.createQuery("""
            select t from Trainee t
            join fetch t.user
            left join fetch t.trainers tr
            left join fetch tr.user
            left join fetch tr.specialization
            where t.id = :id
            """, Trainee.class)
        .setParameter("id", id)
        .getResultStream()
        .findFirst();
  }

  public Optional<Trainee> findByUsername(String username) {
    return entityManager.createQuery("""
            select t from Trainee t
            join fetch t.user
            left join fetch t.trainers tr
            left join fetch tr.user
            left join fetch tr.specialization
            where t.user.username = :username
            """, Trainee.class)
        .setParameter("username", username)
        .getResultStream()
        .findFirst();
  }

  @Override
  public Trainee update(Trainee trainee) {
    Trainee updatedTrainee = entityManager.merge(trainee);
    return entityManager.createQuery("""
              select t from Trainee t
              join fetch t.user
              left join fetch t.trainers tr
              left join fetch tr.user
              left join fetch tr.specialization
              where t.id = :id
              """, Trainee.class)
        .setParameter("id", updatedTrainee.getId())
        .getSingleResult();
  }

  @Override
  public void delete(Trainee trainee) {
    trainee = entityManager.contains(trainee) ? trainee : entityManager.merge(trainee);

    entityManager.createQuery("delete from Training t where t.trainee.id = :traineeId")
        .setParameter("traineeId", trainee.getId())
        .executeUpdate();
    entityManager.remove(trainee);
  }
}
