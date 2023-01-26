package com.example.microservices.users.controller;

import com.example.microservices.users.dto.UserDTO;
import com.example.microservices.users.entity.User;
import com.example.microservices.users.mapper.UserMapper;
import com.example.microservices.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_REQUEST_BODY_CREATE_USER;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_REQUEST_BODY_UPDATE_USER;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_RESPONSE_CREATE_USER_ERROR_412;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_RESPONSE_CREATE_USER_OK_200;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_RESPONSE_DELETE_USER_ERROR_412;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_RESPONSE_DELETE_USER_OK_200;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_RESPONSE_GET_ALL_OK_200;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_RESPONSE_GET_USER_ERROR_404;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_RESPONSE_GET_USER_OK_200;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_RESPONSE_UPDATE_USER_ERROR_422;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_RESPONSE_UPDATE_USER_OK_200;

@Tag(name = "Users", description = "CRUD operations with Users")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Getting a list of all Users")
    @ApiResponse(responseCode = "200", description = "Return all users",
            content = {@Content(mediaType = "application/json", schema = @Schema(allOf = UserDTO.class),
                    examples = {@ExampleObject(value = EXAMPLE_RESPONSE_GET_ALL_OK_200)})})
    @GetMapping
    public List<UserDTO> getAll() {
        return userMapper.toDTOList(userService.getAll());
    }

    @Operation(summary = "Getting User by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class),
                            examples = {@ExampleObject(value = EXAMPLE_RESPONSE_GET_USER_OK_200)})}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(examples = {@ExampleObject(value = EXAMPLE_RESPONSE_GET_USER_ERROR_404)}))})
    @GetMapping(value = "/{id}")
    public UserDTO getUser(@Parameter(description = "id of user to be searched") @PathVariable long id) {
        return userMapper.toDTO(userService.getUser(id));
    }

    @Operation(summary = "User updating")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the user",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class),
                            examples = {@ExampleObject(value = EXAMPLE_RESPONSE_UPDATE_USER_OK_200)})}),
            @ApiResponse(responseCode = "422", description = "UserDTO.id is not match /{id}",
                    content = @Content(examples = {@ExampleObject(value = EXAMPLE_RESPONSE_UPDATE_USER_ERROR_422)}))})
    @PutMapping(value = "/{id}")
    public String updateUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User to be updated",
            required = true, content = @Content(schema = @Schema(implementation = UserDTO.class),
            examples = {@ExampleObject(value = EXAMPLE_REQUEST_BODY_UPDATE_USER)}))
            @RequestBody UserDTO userDTO, @Parameter(description = "id of user to be updated") @PathVariable Long id) {
        User user = userMapper.toEntity(userDTO);
        validateUserWithId(user, id);
        return userService.updateUser(user);
    }

    @Operation(summary = "User creating")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created the user",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class),
                            examples = {@ExampleObject(value = EXAMPLE_RESPONSE_CREATE_USER_OK_200)})}),
            @ApiResponse(responseCode = "412", description = "This nickname already used",
                    content = @Content(examples = {@ExampleObject(value = EXAMPLE_RESPONSE_CREATE_USER_ERROR_412)}))})
    @PostMapping
    public String createUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User to be created",
            required = true, content = @Content(schema = @Schema(implementation = UserDTO.class),
            examples = {@ExampleObject(value = EXAMPLE_REQUEST_BODY_CREATE_USER)})) @RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        return userService.createUser(user);
    }

    @Operation(summary = "User deleting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted the user",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class),
                            examples = {@ExampleObject(value = EXAMPLE_RESPONSE_DELETE_USER_OK_200)})}),
            @ApiResponse(responseCode = "412", description = "User is not exist or already deleted",
                    content = @Content(examples = {@ExampleObject(value = EXAMPLE_RESPONSE_DELETE_USER_ERROR_412)}))})
    @DeleteMapping(value = "/{id}")
    public String deleteUser(@Parameter(description = "id of user to be deleted") @PathVariable Long id) {
        return userService.deleteUser(id);
    }

    private void validateUserWithId(User user, Long id) {
        if (!Objects.equals(user.getId(), id)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
