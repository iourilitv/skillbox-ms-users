package com.example.microservices.users.controller;

import com.example.microservices.users.dto.UserDTO;
import com.example.microservices.users.entity.User;
import com.example.microservices.users.error.entity.ErrorList;
import com.example.microservices.users.error.exception.PreconditionFailedResponseStatusException;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_REQUEST_BODY_CREATE_USER;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_REQUEST_BODY_UPDATE_USER;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_RESPONSE_CREATE_USER_OK_200;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_RESPONSE_CREATE_USER_PRECONDITION_FAILED_ERROR_500;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_RESPONSE_DELETE_USER_OK_200;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_RESPONSE_DELETE_USER_PRECONDITION_FAILED_1_ERROR_500;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_RESPONSE_DELETE_USER_PRECONDITION_FAILED_2_ERROR_500;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_RESPONSE_GET_ALL_OK_200;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_RESPONSE_GET_USER_NOT_FOUND_ERROR_500;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_RESPONSE_GET_USER_OK_200;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_RESPONSE_UPDATE_USER_OK_200;
import static com.example.microservices.users.dictionary.UserControllerDictionary.EXAMPLE_RESPONSE_UPDATE_USER_PRECONDITION_FAILED_ERROR_500;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Users", description = "CRUD operations with Users")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Getting a list of all Users")
    @ApiResponse(responseCode = "200", description = "Return all users",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(allOf = UserDTO.class),
                    examples = {@ExampleObject(value = EXAMPLE_RESPONSE_GET_ALL_OK_200)})})
    @GetMapping
    public List<UserDTO> getAll() {
        List<User> userList = userService.getAll();
        return userMapper.toDTOList(userList);
    }

    @Operation(summary = "Getting User by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class),
                            examples = {@ExampleObject(value = EXAMPLE_RESPONSE_GET_USER_OK_200)})}),
            @ApiResponse(responseCode = "500", description = "404 NOT_FOUND: User not found",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorList.class),
                            examples = {
                            @ExampleObject(value = EXAMPLE_RESPONSE_GET_USER_NOT_FOUND_ERROR_500)
                    }))
    })
    @GetMapping(value = "/{id}")
    public UserDTO getUser(@Parameter(description = "id of user to be searched") @PathVariable long id) {
        User user = userService.getUser(id);
        return userMapper.toDTO(user);
    }

    @Operation(summary = "User updating")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the user",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class),
                            examples = {@ExampleObject(value = EXAMPLE_RESPONSE_UPDATE_USER_OK_200)})}),
            @ApiResponse(responseCode = "500", description = "412 PRECONDITION_FAILED: UserDTO.id is not match /{id}",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorList.class),
                            examples = {
                            @ExampleObject(value = EXAMPLE_RESPONSE_UPDATE_USER_PRECONDITION_FAILED_ERROR_500)
                    }))
    })
    @PutMapping(value = "/{id}")
    public String updateUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User to be updated",
            required = true, content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class),
            examples = {@ExampleObject(value = EXAMPLE_REQUEST_BODY_UPDATE_USER)}))
            @RequestBody UserDTO userDTO, @Parameter(description = "id of user to be updated") @PathVariable Long id) {
        User user = userMapper.toEntity(userDTO);
        validateUserWithId(user, id);
        return userService.updateUser(user);
    }

    @Operation(summary = "User creating")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created the user",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class),
                            examples = {@ExampleObject(value = EXAMPLE_RESPONSE_CREATE_USER_OK_200)})}),
            @ApiResponse(responseCode = "500", description = "412 PRECONDITION_FAILED: This nickname already used",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorList.class),
                            examples = {
                            @ExampleObject(value = EXAMPLE_RESPONSE_CREATE_USER_PRECONDITION_FAILED_ERROR_500)
                    }))
    })
    @PostMapping
    public UserDTO createUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User to be created",
            required = true, content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class),
            examples = {@ExampleObject(value = EXAMPLE_REQUEST_BODY_CREATE_USER)})) @RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User savedUser = userService.createUser(user);
        return userMapper.toDTO(savedUser);
    }

    @Operation(summary = "User deleting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted the user",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class),
                            examples = {@ExampleObject(value = EXAMPLE_RESPONSE_DELETE_USER_OK_200)})),
            @ApiResponse(responseCode = "500(1)", description = "412 PRECONDITION_FAILED: User is not exist",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorList.class),
                            examples = {
                            @ExampleObject(value = EXAMPLE_RESPONSE_DELETE_USER_PRECONDITION_FAILED_1_ERROR_500)
                    })),
            @ApiResponse(responseCode = "500(2)", description = "412 PRECONDITION_FAILED: User is already deleted",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorList.class),
                            examples = {
                            @ExampleObject(value = EXAMPLE_RESPONSE_DELETE_USER_PRECONDITION_FAILED_2_ERROR_500)
                    }))
    })
    @DeleteMapping(value = "/{id}")
    public String deleteUser(@Parameter(description = "id of user to be deleted") @PathVariable Long id) {
        return userService.deleteUser(id);
    }

    private void validateUserWithId(User user, Long id) {
        if (!Objects.equals(user.getId(), id)) {
            String reason = String.format("Values of User.id(%d) And id(%d) argument Are Not Equal", user.getId(), id);
            throw new PreconditionFailedResponseStatusException(reason);
        }
    }
}
