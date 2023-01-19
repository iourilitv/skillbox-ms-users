package com.example.microservices.users.service;

import com.example.microservices.users.entity.Follow;
import com.example.microservices.users.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FollowService {

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
        return followRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public String createFollow(Follow follow) {
        if (Objects.equals(follow.getFollowingId(), follow.getFollowerId())
                || followRepository.findByFollowingIdAndFollowerId(follow.getFollowingId(), follow.getFollowerId()).isPresent()
                    ) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED);
        }
        follow.setFollowedAt(follow.getFollowedAt());
        Follow savedFollow = followRepository.save(follow);
        return String.format("User(id: %s) has been followed to User(id: %s) with Follow(id: %s)",
                savedFollow.getFollowingId(), savedFollow.getFollowerId(), savedFollow.getId());
    }

    @Transactional
    public String deleteFollow(Long id) {
        Optional<Follow> existFollowOptional = followRepository.findById(id);
        if (existFollowOptional.isPresent()) {
            followRepository.deleteById(id);
            Follow follow = existFollowOptional.get();
            return String.format("User(id: %s) has been followed out from User(id: %s). The Follow(id: %s) has been deleted",
                    follow.getFollowingId(), follow.getFollowerId(), follow.getId());
        }
        return String.format("There is no Follow to delete with id: %s", id);
    }

    public void setRefersDeletedUserInAllWhereFollowingIdOrFollowerId(Long userId, boolean flag) {
        followRepository.setRefersDeletedUserInAllWhereFollowingIdOrFollowerId(userId, flag);
    }
}
