package com.example.microservices.users.service;

import com.example.microservices.users.entity.User;
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
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public String updateUser(User user) {
        User savedUser = userRepository.save(user);
        return String.format("User(id: %s, nickname: %s) has been updated successfully", savedUser.getId(), savedUser.getNickname());
    }

    public String createUser(User user) {
        if (userRepository.findByNickname(user.getNickname()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED);
        }
        User savedUser = userRepository.save(user);
        return String.format("New user(nickname: %s) has been saved with id: %s", savedUser.getNickname(), savedUser.getId());
    }

    @Transactional
    public String deleteUser(Long id) {
        Optional<User> userInDbOptional = userRepository.findById(id);
        if (userInDbOptional.isEmpty() || userInDbOptional.get().isDeleted()) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED);
        }
        User user = userInDbOptional.get();
        userRepository.delete(user);
        followService.setRefersDeletedUserInAllWhereFollowingIdOrFollowerId(user.getId(), true);
        return String.format("User(id: %s, nickname: %s) has been deleted", user.getId(), user.getNickname());
    }
}
