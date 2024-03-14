package com.epam.gymapp.controller.impl;

import com.epam.gymapp.controller.TrainingController;
import com.epam.gymapp.dto.TrainingCreateDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.jwt.JwtProcess;
import com.epam.gymapp.logging.LoggerHelper;
import com.epam.gymapp.service.TrainingService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TrainingControllerImpl implements TrainingController {

  private static final Logger log = LoggerFactory.getLogger(TrainingControllerImpl.class);

  private final TrainingService trainingService;
  private final JwtProcess jwtProcess;

  private final LoggerHelper loggerHelper;

  @Override
  public void addTraining(TrainingCreateDto trainingCreateDto, HttpServletRequest request) {
    loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}, Body: {}",
          request.getRequestURI(), request.getMethod(), trainingCreateDto);

      jwtProcess.processToken(request);
      trainingService.addTraining(trainingCreateDto);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return null;
    });
  }

  @Override
  public List<TrainingInfoDto> selectTrainings(HttpServletRequest request) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}",
          request.getRequestURI(), request.getMethod());

      jwtProcess.processToken(request);
      List<TrainingInfoDto> trainings = trainingService.selectTrainings();

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return trainings;
    });
  }
}
