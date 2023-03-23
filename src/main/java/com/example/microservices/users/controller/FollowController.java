package com.example.microservices.users.controller;

import com.example.microservices.users.dto.FollowDTO;
import com.example.microservices.users.entity.Follow;
import com.example.microservices.users.error.exception.PreconditionFailedResponseStatusException;
import com.example.microservices.users.mapper.FollowMapper;
import com.example.microservices.users.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/follows")
public class FollowController {

    private final FollowService followService;
    private final FollowMapper followMapper;

    @GetMapping
    public List<FollowDTO> getAll() {
        return followMapper.toDTOList(followService.getAll());
    }

    @GetMapping("/followings/{followerId}")
    public List<FollowDTO> getAllFollowings(@PathVariable Long followerId) {
        return followMapper.toDTOList(followService.getAllFollowings(followerId));
    }

    @GetMapping("/followers/{followingId}")
    public List<FollowDTO> getAllFollowers(@PathVariable Long followingId) {
        return followMapper.toDTOList(followService.getAllFollowers(followingId));
    }

    @GetMapping(value = "/{id}")
    public FollowDTO getFollow(@PathVariable long id) {
        return followMapper.toDTO(followService.getFollow(id));
    }

    @PostMapping
    public FollowDTO createFollow(@RequestBody FollowDTO followDTO) {
        verifyFollowDTO(followDTO);
        Follow follow = followMapper.toEntity(followDTO);
        Follow savedFollow = followService.createFollow(follow);
        return followMapper.toDTO(savedFollow);
    }

    @DeleteMapping(value = "/{id}")
    public String deleteFollow(@PathVariable Long id) {
        return followService.deleteFollow(id);
    }

    private void verifyFollowDTO(FollowDTO followDTO) {
        if (followDTO.getId() != null) {
            throw new PreconditionFailedResponseStatusException("FollowDTO.id Must Be null Or Excluded");
        }
    }
}
