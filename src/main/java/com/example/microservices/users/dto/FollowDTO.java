package com.example.microservices.users.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@ToString
@Data
@EqualsAndHashCode
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FollowDTO {

    private Long id;

    @JsonProperty(required = true)
    private Long followingId;

    @JsonProperty(required = true)
    private Long followerId;

    private Date followedAt;

    public FollowDTO(Long followingId, Long followerId) {
        this.followingId = followingId;
        this.followerId = followerId;
    }
}
