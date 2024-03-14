package com.epam.gymapp.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.gymapp.GymAppApplication;
import com.epam.gymapp.config.TestHibernateConfiguration;
import com.epam.gymapp.model.TrainingType;
import com.epam.gymapp.test.utils.TrainingTypeTestUtil;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
@ContextConfiguration(classes = {GymAppApplication.class, TestHibernateConfiguration.class})
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TrainingTypeRepositoryTest {

  private static final Logger log = LoggerFactory.getLogger(TrainingTypeRepositoryTest.class);

  @Autowired
  private TrainingTypeRepository trainingTypeRepository;

  @Test
  @Order(1)
  void save_Success() {
    // Given
    TrainingType trainingType = TrainingTypeTestUtil.getNewTrainingType4();

    // When
    TrainingType result = trainingTypeRepository.save(trainingType);

    // Then
    assertThat(result, allOf(
        notNullValue(),
        hasProperty("id", greaterThan(3L)),
        hasProperty("name", equalTo(trainingType.getName()))
    ));
  }

  @Test
  @Order(2)
  void findAll_Success() {
    // When
    List<TrainingType> result = trainingTypeRepository.findAll();

    // Then
    assertThat(result, allOf(
        notNullValue(),
        hasSize(greaterThanOrEqualTo(3))
    ));
  }

  @Test
  @Order(3)
  void findById_IfExistsReturnEntity_Success() {
    // Given
    long trainingTypeId = TrainingTypeTestUtil.TEST_TRAINING_TYPE_ID_1;

    // When
    Optional<TrainingType> result = trainingTypeRepository.findById(trainingTypeId);

    // Then
    assertTrue(result.isPresent());
    assertThat(result.get().getId(), equalTo(trainingTypeId));
  }

  @Test
  @Order(4)
  void findById_IfAbsentReturnNull_Success() {
    // Given
    long trainingTypeId = 10;

    // When
    Optional<TrainingType> result = trainingTypeRepository.findById(trainingTypeId);

    // Then
    assertTrue(result.isEmpty());
  }

  @Test
  @Order(5)
  void update_Success() {
    // Given
    TrainingType trainingType = TrainingTypeTestUtil.getTrainingType1();
    trainingType.setName(TrainingTypeTestUtil.TEST_TRAINING_TYPE_NAME_5);

    // When
    TrainingType result = trainingTypeRepository.update(trainingType);

    // Then
    assertThat(result, allOf(
        notNullValue(),
        hasProperty("id", equalTo(trainingType.getId())),
        hasProperty("name", equalTo(trainingType.getName()))
    ));
  }

  @Test
  @Order(6)
  void delete_Success() {
    // Given
    TrainingType trainingType = TrainingTypeTestUtil.getTrainingType4();

    // When
    trainingTypeRepository.delete(trainingType);
  }
}
