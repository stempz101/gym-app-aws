package com.epam.gymapp.controller;

import com.epam.gymapp.dto.ErrorMessageDto;
import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.dto.TrainerInfoDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.dto.TrainerUpdateDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/api/trainers")
@Tag(name = "Trainers", description = "Trainer management API")
public interface TrainerController {

  @PostMapping
  @Operation(summary = "Creating trainer", tags = {"Trainers"}, responses = {
      @ApiResponse(responseCode = "201", description = "Trainer successfully created",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = UserCredentialsDto.class)
          )
      ),
      @ApiResponse(responseCode = "400", description = "Specified wrong fields",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "500", description = "Application failed to process the request")
  })
  @ResponseStatus(HttpStatus.CREATED)
  UserCredentialsDto createTrainer(@RequestBody @Valid TrainerCreateDto trainerCreateDto,
      HttpServletRequest request);

  @GetMapping
  @Operation(summary = "Selecting trainers", tags = {"Trainers"},
      security = @SecurityRequirement(name = "bearerAuth"), responses = {
      @ApiResponse(responseCode = "200", description = "List of trainers successfully returned",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = TrainerInfoDto.class))
          )
      ),
      @ApiResponse(responseCode = "401", description = "Attempted unauthorized access",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "500", description = "Application failed to process the request")
  })
  List<TrainerInfoDto> selectTrainers(HttpServletRequest request);

  @GetMapping("/{trainerUsername}")
  @Operation(summary = "Selecting trainer", tags = {"Trainers"},
      security = @SecurityRequirement(name = "bearerAuth"), responses = {
      @ApiResponse(responseCode = "200", description = "Trainer information successfully returned",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = TrainerInfoDto.class)
          )
      ),
      @ApiResponse(responseCode = "401", description = "Attempted unauthorized access",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "404", description = "Trainer not found",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "500", description = "Application failed to process the request")
  })
  TrainerInfoDto selectTrainer(@PathVariable String trainerUsername, HttpServletRequest request);

  @PutMapping
  @Operation(summary = "Updating trainer", tags = {"Trainers"},
      security = @SecurityRequirement(name = "bearerAuth"), responses = {
      @ApiResponse(responseCode = "200", description = "Trainer successfully updated",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = TrainerInfoDto.class)
          )
      ),
      @ApiResponse(responseCode = "400", description = "Specified wrong fields",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "401", description = "Attempted unauthorized access",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "404", description = "Trainer not found",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "500", description = "Application failed to process the request")
  })
  TrainerInfoDto updateTrainer(@RequestBody @Valid TrainerUpdateDto trainerUpdateDto,
      HttpServletRequest request);

  @GetMapping("/unassigned/{traineeUsername}")
  @Operation(summary = "Retrieving unassigned trainers on specified trainee", tags = {"Trainers"},
      security = @SecurityRequirement(name = "bearerAuth"), responses = {
      @ApiResponse(responseCode = "200", description = "List of unassigned trainers successfully returned",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = TrainerShortInfoDto.class))
          )
      ),
      @ApiResponse(responseCode = "401", description = "Attempted unauthorized access",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "500", description = "Application failed to process the request")
  })
  List<TrainerShortInfoDto> getUnassignedTrainersOnTrainee(@PathVariable String traineeUsername,
      HttpServletRequest request);

  @GetMapping("/trainings")
  @Operation(summary = "Retrieving trainer's trainings", tags = {"Trainers"},
      security = @SecurityRequirement(name = "bearerAuth"), responses = {
      @ApiResponse(responseCode = "200", description = "List of trainer's trainings successfully returned",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = TrainingInfoDto.class))
          )
      ),
      @ApiResponse(responseCode = "400", description = "Missing required parameters",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "401", description = "Attempted unauthorized access",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "500", description = "Application failed to process the request")
  })
  List<TrainingInfoDto> getTrainerTrainings(
      @RequestParam(name = "username") String trainerUsername,
      @RequestParam(name = "fromDate", required = false) String fromDate,
      @RequestParam(name = "toDate", required = false) String toDate,
      @RequestParam(name = "traineeName", required = false) String traineeName,
      HttpServletRequest request);
}
