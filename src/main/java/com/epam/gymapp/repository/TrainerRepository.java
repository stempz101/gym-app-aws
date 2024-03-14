package com.epam.gymapp.repository;

import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerRepository implements GeneralRepository<Long, Trainer> {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Trainer save(Trainer trainer) {
    entityManager.createQuery("""
                select tt from TrainingType tt
                where tt.name = :name
                """, TrainingType.class)
        .setParameter("name", trainer.getSpecialization().getName())
        .getResultStream()
        .findFirst()
        .ifPresent(trainer::setSpecialization);

    entityManager.persist(trainer);
    return trainer;
  }

  @Override
  public List<Trainer> findAll() {
    return entityManager.createQuery("""
            select t from Trainer t
            join fetch t.user
            join fetch t.specialization
            left join fetch t.trainees tr
            left join fetch tr.user
            """, Trainer.class)
        .getResultList();
  }

  public List<Trainer> findAllUnassignedByTraineeUsername(String traineeUsername) {
    return entityManager.createQuery("""
            select tr1 from Trainer tr1
            join fetch tr1.user
            join fetch tr1.specialization
            left join fetch tr1.trainees
            where tr1.id not in (
              select tr2.id
              from Trainer tr2
              join tr2.trainees te
              join te.user ute
              where ute.username = :traineeUsername
            )
            """, Trainer.class)
        .setParameter("traineeUsername", traineeUsername)
        .getResultList();
  }

  public List<Trainer> findAllByUsernameIn(List<String> usernames) {
    return entityManager.createQuery("""
            select t from Trainer t
            join fetch t.user u
            join fetch t.specialization
            left join fetch t.trainees
            where u.username in (:usernames)
            """, Trainer.class)
        .setParameter("usernames", usernames)
        .getResultList();
  }

  @Override
  public Optional<Trainer> findById(Long id) {
    return entityManager.createQuery("""
            select t from Trainer t
            join fetch t.user
            join fetch t.specialization
            left join fetch t.trainees tr
            left join fetch tr.user
            where t.id = :id
            """, Trainer.class)
        .setParameter("id", id)
        .getResultStream()
        .findFirst();
  }

  public Optional<Trainer> findByUsername(String username) {
    return entityManager.createQuery("""
            select t from Trainer t
            join fetch t.user
            join fetch t.specialization
            left join fetch t.trainees tr
            left join fetch tr.user
            where t.user.username = :username
            """, Trainer.class)
        .setParameter("username", username)
        .getResultStream()
        .findFirst();
  }

  @Override
  public Trainer update(Trainer trainer) {
    Trainer updatedTrainer = entityManager.merge(trainer);
    return entityManager.createQuery("""
            select t from Trainer t
            join fetch t.user
            join fetch t.specialization
            left join fetch t.trainees tr
            left join fetch tr.user
            where t.id = :id
            """, Trainer.class)
        .setParameter("id", updatedTrainer.getId())
        .getSingleResult();
  }

  @Override
  public void delete(Trainer trainer) {
    trainer = entityManager.contains(trainer) ? trainer : entityManager.merge(trainer);

    entityManager.createQuery("delete from Training t where t.trainer.id = :trainerId")
        .setParameter("trainerId", trainer.getId())
        .executeUpdate();
    entityManager.remove(trainer);
  }
}
