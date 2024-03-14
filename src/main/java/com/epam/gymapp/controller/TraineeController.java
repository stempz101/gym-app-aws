package com.epam.gymapp.controller;

import com.epam.gymapp.dto.ErrorMessageDto;
import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.dto.TraineeInfoDto;
import com.epam.gymapp.dto.TraineeTrainersUpdateDto;
import com.epam.gymapp.dto.TraineeUpdateDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/api/trainees")
@Tag(name = "Trainees", description = "Trainee management API")
public interface TraineeController {

  @PostMapping
  @Operation(summary = "Creating trainee", tags = "Trainees", responses = {
      @ApiResponse(responseCode = "201", description = "Trainee successfully created",
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
  UserCredentialsDto createTrainee(@RequestBody @Valid TraineeCreateDto traineeCreateDto,
      HttpServletRequest request);

  @GetMapping
  @Operation(summary = "Selecting trainees", tags = "Trainees",
      security = @SecurityRequirement(name = "bearerAuth"), responses = {
      @ApiResponse(responseCode = "200", description = "List of trainees successfully returned",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = TraineeInfoDto.class))
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
  List<TraineeInfoDto> selectTrainees(HttpServletRequest request);

  @GetMapping("/{traineeUsername}")
  @Operation(summary = "Selecting trainee", tags = "Trainees",
      security = @SecurityRequirement(name = "bearerAuth"), responses = {
      @ApiResponse(responseCode = "200", description = "Trainee information successfully returned",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = TraineeInfoDto.class)
          )
      ),
      @ApiResponse(responseCode = "401", description = "Attempted unauthorized access",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "404", description = "Trainee not found",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "500", description = "Application failed to process the request")
  })
  TraineeInfoDto selectTrainee(@PathVariable String traineeUsername, HttpServletRequest request);

  @PutMapping
  @Operation(summary = "Updating trainee", tags = "Trainees",
      security = @SecurityRequirement(name = "bearerAuth"), responses = {
      @ApiResponse(responseCode = "200", description = "Trainee successfully updated",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = TraineeInfoDto.class)
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
      @ApiResponse(responseCode = "404", description = "Trainee not found",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "500", description = "Application failed to process the request")
  })
  TraineeInfoDto updateTrainee(@RequestBody @Valid TraineeUpdateDto traineeUpdateDto,
      HttpServletRequest request);

  @DeleteMapping("/{traineeUsername}")
  @Operation(summary = "Deleting trainee", tags = "Trainees",
      security = @SecurityRequirement(name = "bearerAuth"), responses = {
      @ApiResponse(responseCode = "200", description = "Trainee successfully deleted"),
      @ApiResponse(responseCode = "401", description = "Attempted unauthorized access",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "404", description = "Trainee not found",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "500", description = "Application failed to process the request")
  })
  void deleteTrainee(@PathVariable String traineeUsername, HttpServletRequest request);

  @PutMapping("/trainers")
  @Operation(summary = "Updating trainee's trainer list", tags = "Trainees",
      security = @SecurityRequirement(name = "bearerAuth"), responses = {
      @ApiResponse(responseCode = "200", description = "Trainee's trainer list successfully updated",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = TrainerShortInfoDto.class))
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
      @ApiResponse(responseCode = "404", description = "Trainee or trainers not found",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "500", description = "Application failed to process the request")
  })
  List<TrainerShortInfoDto> updateTrainerListOfTrainee(
      @RequestBody @Valid TraineeTrainersUpdateDto traineeTrainersUpdateDto,
      HttpServletRequest request);

  @GetMapping("/trainings")
  @Operation(summary = "Retrieving trainee's trainings", tags = "Trainees",
      security = @SecurityRequirement(name = "bearerAuth"), responses = {
      @ApiResponse(responseCode = "200", description = "List of trainee's trainings successfully returned",
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
  List<TrainingInfoDto> getTraineeTrainings(
      @RequestParam(name = "username") String traineeUsername,
      @RequestParam(name = "fromDate", required = false) String fromDate,
      @RequestParam(name = "toDate", required = false) String toDate,
      @RequestParam(name = "trainerName", required = false) String trainerName,
      @RequestParam(name = "trainingType", required = false) String trainingType,
      HttpServletRequest request);
}
