package com.example.microservices.users.service;

import com.example.microservices.users.entity.Follow;
import com.example.microservices.users.error.exception.FollowNotFoundResponseStatusException;
import com.example.microservices.users.error.exception.PreconditionFailedResponseStatusException;
import com.example.microservices.users.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FollowService {
    private static final String RESOURCE = "Follow";

    private final FollowRepository followRepository;

    public List<Follow> getAll() {
        return (List<Follow>) followRepository.findAll();
    }

    public List<Follow> getAllFollowings(Long followerId) {
        return followRepository.findAllByFollowerId(followerId);
    }

    public List<Follow> getAllFollowers(Long followingId) {
        return followRepository.findAllByFollowingId(followingId);
    }

    public Follow getFollow(long id) {
        return followRepository.findById(id).orElseThrow(() -> new FollowNotFoundResponseStatusException(id));
    }

    @Transactional
    public Follow createFollow(Follow follow) {
        if (Objects.equals(follow.getFollowingId(), follow.getFollowerId())) {
            throw new PreconditionFailedResponseStatusException(
                    String.format(
                            "Following And Follower Must Be Not The Same Person But Got The Same followingId: %s and followerId: %s",
                            follow.getFollowingId(), follow.getFollowerId()
                    ));
        }
        if (followRepository.findByFollowingIdAndFollowerId(follow.getFollowingId(), follow.getFollowerId()).isPresent()) {
            throw new PreconditionFailedResponseStatusException(
                    String.format("%s(followingId: %s, followerId: %s) Already Exists",
                            RESOURCE, follow.getFollowingId(), follow.getFollowerId())
            );
        }
        follow.setFollowedAt(follow.getFollowedAt());
        return followRepository.save(follow);
    }

    @Transactional
    public String deleteFollow(Long id) {
        Optional<Follow> existFollowOptional = followRepository.findById(id);
        if (existFollowOptional.isPresent()) {
            followRepository.deleteById(id);
            Follow follow = existFollowOptional.get();
            return String.format("User(id: %s) has been followed out from User(id: %s). The %s(id: %s) has been deleted",
                    follow.getFollowingId(), follow.getFollowerId(), RESOURCE, follow.getId());
        }
        return String.format("There is no %s to delete with id: %s", RESOURCE, id);
    }

    public void setRefersDeletedUserInAllWhereFollowingIdOrFollowerId(Long userId, boolean flag) {
        followRepository.setRefersDeletedUserInAllWhereFollowingIdOrFollowerId(userId, flag);
    }
}
