package com.epam.gymapp.repository;

import com.epam.gymapp.model.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingTypeRepository implements GeneralRepository<Long, TrainingType> {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public TrainingType save(TrainingType trainingType) {
    entityManager.persist(trainingType);
    return trainingType;
  }

  @Override
  public List<TrainingType> findAll() {
    return entityManager.createQuery("select tt from TrainingType tt", TrainingType.class)
        .getResultList();
  }

  @Override
  public Optional<TrainingType> findById(Long id) {
    return entityManager.createQuery("""
            select tt from TrainingType tt
            where tt.id = :id
            """, TrainingType.class)
        .setParameter("id", id)
        .getResultStream()
        .findFirst();
  }

  @Override
  public TrainingType update(TrainingType trainingType) {
    return entityManager.merge(trainingType);
  }

  @Override
  public void delete(TrainingType trainingType) {
    entityManager.remove(
        entityManager.contains(trainingType) ? trainingType : entityManager.merge(trainingType));
  }
}
