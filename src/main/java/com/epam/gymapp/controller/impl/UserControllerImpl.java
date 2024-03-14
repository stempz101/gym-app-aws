package com.epam.gymapp.controller.impl;

import com.epam.gymapp.controller.UserController;
import com.epam.gymapp.dto.ChangePasswordDto;
import com.epam.gymapp.dto.JwtDto;
import com.epam.gymapp.dto.UserActivateDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.jwt.JwtProcess;
import com.epam.gymapp.logging.LoggerHelper;
import com.epam.gymapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

  private static final Logger log = LoggerFactory.getLogger(UserControllerImpl.class);

  private final UserService userService;
  private final JwtProcess jwtProcess;

  private final LoggerHelper loggerHelper;

  @Override
  public JwtDto authenticate(UserCredentialsDto userCredentialsDto, HttpServletRequest request) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}, Body: {}",
          request.getRequestURI(), request.getMethod(), userCredentialsDto);

      JwtDto jwtDto = userService.authenticate(userCredentialsDto);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return jwtDto;
    });
  }

  @Override
  public void changePassword(ChangePasswordDto changePasswordDto, HttpServletRequest request) {
    loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}, Body: {}",
          request.getRequestURI(), request.getMethod(), changePasswordDto);

      userService.changePassword(changePasswordDto);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return null;
    });
  }

  @Override
  public void changeActivationStatus(UserActivateDto userActivateDto, HttpServletRequest request) {
    loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}, Body: {}",
          request.getRequestURI(), request.getMethod(), userActivateDto);

      jwtProcess.processToken(request);
      userService.changeActivationStatus(userActivateDto);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return null;
    });
  }
}
