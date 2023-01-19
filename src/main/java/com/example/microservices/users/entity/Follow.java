package com.example.microservices.users.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Used pattern Soft Delete for deletedAt field.
 * Source "How to Implement Soft Delete in Spring JPA.": https://www.baeldung.com/spring-jpa-soft-delete
 */

@ToString
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "follows")
@Where(clause = "refers_deleted_user = false")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "following_id", nullable = false, updatable = false)
    private Long followingId;

    @Column(name = "follower_id", nullable = false, updatable = false)
    private Long followerId;

    @Column(name = "followed_at", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date followedAt;

    @Column(name = "refers_deleted_user")
    private boolean refersDeletedUser;

    public Follow(Long followingId, Long followerId) {
        this.followingId = followingId;
        this.followerId = followerId;
        this.followedAt = new Date();
    }

    public void setFollowedAt(Date followedAt) {
        this.followedAt = followedAt == null ? new Date() : followedAt;
    }
}
