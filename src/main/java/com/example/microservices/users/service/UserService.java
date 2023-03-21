package com.example.microservices.users.service;

import com.example.microservices.users.entity.User;
import com.example.microservices.users.error.exception.PreconditionFailedResponseStatusException;
import com.example.microservices.users.error.exception.UserNotFoundResponseStatusException;
import com.example.microservices.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final FollowService followService;

    public List<User> getAll() {
        return (List<User>) userRepository.findAll();
    }

    public User getUser(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundResponseStatusException(id));
    }

    public String updateUser(User user) {
        User savedUser = userRepository.save(user);
        return String.format("User(id: %s, nickname: %s) has been updated successfully", savedUser.getId(), savedUser.getNickname());
    }

    public User createUser(User user) {
        if (userRepository.findByNicknameIncludingDeleted(user.getNickname()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED);
        }
        return userRepository.save(user);
    }

    @Transactional
    public String deleteUser(Long id) {
        Optional<User> userInDbOptional = userRepository.findById(id);
        if (userInDbOptional.isEmpty()) {
            throw new PreconditionFailedResponseStatusException("User(id: " + id + ") Does Not Exist");
        } else if(userInDbOptional.get().isDeleted()) {
            throw new PreconditionFailedResponseStatusException("User(id: " + id + ") Is Already Deleted");
        }
        User user = userInDbOptional.get();
        userRepository.delete(user);
        followService.setRefersDeletedUserInAllWhereFollowingIdOrFollowerId(user.getId(), true);
        return String.format("User(id: %s, nickname: %s) has been deleted", user.getId(), user.getNickname());
    }
}
