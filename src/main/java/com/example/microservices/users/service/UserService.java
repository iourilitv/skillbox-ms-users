package com.example.microservices.users.service;

import com.example.microservices.users.entity.User;
import com.example.microservices.users.error.exception.PreconditionFailedResponseStatusException;
import com.example.microservices.users.error.exception.UserNotFoundResponseStatusException;
import com.example.microservices.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private static final String RESOURCE = "User";

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
        return String.format("%s(id: %s, nickname: %s) has been updated successfully", RESOURCE, savedUser.getId(), savedUser.getNickname());
    }

    public User createUser(User user) {
        if (userRepository.findByNicknameIncludingDeleted(user.getNickname()).isPresent()) {
            throw new PreconditionFailedResponseStatusException(
                    String.format("%s(nickname: %s) Already Exists Including Deleted", RESOURCE, user.getNickname()));
        }
        return userRepository.save(user);
    }

    @Transactional
    public String deleteUser(Long id) {
        Optional<User> userInDbOptional = userRepository.findById(id);
        if (userInDbOptional.isEmpty()) {
            throw new PreconditionFailedResponseStatusException(String.format("%s(id: %d) Does Not Exist", RESOURCE, id));
        } else if(userInDbOptional.get().isDeleted()) {
            throw new PreconditionFailedResponseStatusException(String.format("%s(id: %d) Is Already Deleted", RESOURCE, id));
        }
        User user = userInDbOptional.get();
        userRepository.delete(user);
        followService.setRefersDeletedUserInAllWhereFollowingIdOrFollowerId(user.getId(), true);
        return String.format("%s(id: %s, nickname: %s) has been deleted", RESOURCE, user.getId(), user.getNickname());
    }
}
