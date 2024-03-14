package com.epam.gymapp.controller.impl;

import com.epam.gymapp.controller.TrainerController;
import com.epam.gymapp.controller.utils.ControllerUtils;
import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.dto.TrainerInfoDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.dto.TrainerUpdateDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.jwt.JwtProcess;
import com.epam.gymapp.logging.LoggerHelper;
import com.epam.gymapp.service.TrainerService;
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
public class TrainerControllerImpl implements TrainerController {

  private static final Logger log = LoggerFactory.getLogger(TrainerControllerImpl.class);

  private final TrainerService trainerService;
  private final JwtProcess jwtProcess;

  private final LoggerHelper loggerHelper;

  @Override
  public UserCredentialsDto createTrainer(TrainerCreateDto trainerCreateDto,
      HttpServletRequest request) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}, Body: {}",
          request.getRequestURI(), request.getMethod(), trainerCreateDto);

      UserCredentialsDto userCredentialsDto = trainerService.createTrainer(trainerCreateDto);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.CREATED.value());
      return userCredentialsDto;
    });
  }

  @Override
  public List<TrainerInfoDto> selectTrainers(HttpServletRequest request) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}",
          request.getRequestURI(), request.getMethod());

      jwtProcess.processToken(request);
      List<TrainerInfoDto> trainerInfoDtos = trainerService.selectTrainers();

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return trainerInfoDtos;
    });
  }

  @Override
  public TrainerInfoDto selectTrainer(String trainerUsername, HttpServletRequest request) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}",
          request.getRequestURI(), request.getMethod());

      jwtProcess.processToken(request);
      TrainerInfoDto trainerInfoDto = trainerService.selectTrainer(trainerUsername);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return trainerInfoDto;
    });
  }

  @Override
  public TrainerInfoDto updateTrainer(TrainerUpdateDto trainerUpdateDto,
      HttpServletRequest request) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}, Body: {}",
          request.getRequestURI(), request.getMethod(), trainerUpdateDto);

      jwtProcess.processToken(request);
      TrainerInfoDto trainerInfoDto = trainerService.updateTrainer(trainerUpdateDto);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return trainerInfoDto;
    });
  }

  @Override
  public List<TrainerShortInfoDto> getUnassignedTrainersOnTrainee(String traineeUsername,
      HttpServletRequest request) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}",
          request.getRequestURI(), request.getMethod());

      jwtProcess.processToken(request);
      List<TrainerShortInfoDto> unassignedTrainers = trainerService
          .findUnassignedTrainers(traineeUsername);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return unassignedTrainers;
    });
  }

  @Override
  public List<TrainingInfoDto> getTrainerTrainings(String trainerUsername, String fromDate,
      String toDate, String traineeName, HttpServletRequest request) {
    return loggerHelper.transactionalLogging(() -> {
      Map<String, List<String>> params = ControllerUtils.getRequestParams(request);
      log.info("REST call received - Endpoint: '{}', Method: {}, Parameters: {}",
          request.getRequestURI(), request.getMethod(), params);

      jwtProcess.processToken(request);

      LocalDate parsedFromDate = ControllerUtils.parseStringToLocalDate(fromDate);
      LocalDate parsedToDate = ControllerUtils.parseStringToLocalDate(toDate);

      List<TrainingInfoDto> trainerTrainings = trainerService
          .findTrainerTrainings(trainerUsername, parsedFromDate, parsedToDate, traineeName);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Parameters: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), params, HttpStatus.OK.value());
      return trainerTrainings;
    });
  }
}
