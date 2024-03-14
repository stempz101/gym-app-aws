package com.epam.gymapp.controller.impl;

import com.epam.gymapp.controller.TraineeController;
import com.epam.gymapp.controller.utils.ControllerUtils;
import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.dto.TraineeInfoDto;
import com.epam.gymapp.dto.TraineeTrainersUpdateDto;
import com.epam.gymapp.dto.TraineeUpdateDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.jwt.JwtProcess;
import com.epam.gymapp.logging.LoggerHelper;
import com.epam.gymapp.service.TraineeService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TraineeControllerImpl implements TraineeController {

  private static final Logger log = LoggerFactory.getLogger(TraineeControllerImpl.class);

  private final TraineeService traineeService;
  private final JwtProcess jwtProcess;

  private final LoggerHelper loggerHelper;

  @Override
  public UserCredentialsDto createTrainee(TraineeCreateDto traineeCreateDto,
      HttpServletRequest request) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}, Body: {}",
          request.getRequestURI(), request.getMethod(), traineeCreateDto);

      UserCredentialsDto userCredentialsDto = traineeService.createTrainee(traineeCreateDto);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.CREATED.value());
      return userCredentialsDto;
    });
  }

  @Override
  public List<TraineeInfoDto> selectTrainees(HttpServletRequest request) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}",
          request.getRequestURI(), request.getMethod());

      jwtProcess.processToken(request);
      List<TraineeInfoDto> traineeInfoDtos = traineeService.selectTrainees();

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return traineeInfoDtos;
    });
  }

  @Override
  public TraineeInfoDto selectTrainee(String traineeUsername, HttpServletRequest request) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}",
          request.getRequestURI(), request.getMethod());

      jwtProcess.processToken(request);
      TraineeInfoDto traineeInfoDto = traineeService.selectTrainee(traineeUsername);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return traineeInfoDto;
    });
  }

  @Override
  public TraineeInfoDto updateTrainee(TraineeUpdateDto traineeUpdateDto,
      HttpServletRequest request) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}, Body: {}",
          request.getRequestURI(), request.getMethod(), traineeUpdateDto);

      jwtProcess.processToken(request);
      TraineeInfoDto traineeInfoDto = traineeService.updateTrainee(traineeUpdateDto);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return traineeInfoDto;
    });
  }

  @Override
  public void deleteTrainee(String traineeUsername, HttpServletRequest request) {
    loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}",
          request.getRequestURI(), request.getMethod());

      jwtProcess.processToken(request);
      traineeService.deleteTrainee(traineeUsername);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return null;
    });
  }

  @Override
  public List<TrainerShortInfoDto> updateTrainerListOfTrainee(
      TraineeTrainersUpdateDto traineeTrainersUpdateDto, HttpServletRequest request) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}, Body: {}",
          request.getRequestURI(), request.getMethod(), traineeTrainersUpdateDto);

      jwtProcess.processToken(request);
      List<TrainerShortInfoDto> trainerShortInfoDtos = traineeService
          .updateTrainerList(traineeTrainersUpdateDto);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return trainerShortInfoDtos;
    });
  }

  @Override
  public List<TrainingInfoDto> getTraineeTrainings(String traineeUsername, String fromDate,
      String toDate, String trainerName, String trainingType, HttpServletRequest request) {
    return loggerHelper.transactionalLogging(() -> {
      Map<String, List<String>> params = ControllerUtils.getRequestParams(request);
      log.info("REST call received - Endpoint: '{}', Method: {}, Parameters: {}",
          request.getRequestURI(), request.getMethod(), params);

      jwtProcess.processToken(request);

      LocalDate parsedFromDate = ControllerUtils.parseStringToLocalDate(fromDate);
      LocalDate parsedToDate = ControllerUtils.parseStringToLocalDate(toDate);

      List<TrainingInfoDto> traineeTrainings = traineeService
          .findTraineeTrainings(traineeUsername, parsedFromDate, parsedToDate, trainerName,
              trainingType);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Parameters: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), params, HttpStatus.OK.value());
      return traineeTrainings;
    });
  }
}
