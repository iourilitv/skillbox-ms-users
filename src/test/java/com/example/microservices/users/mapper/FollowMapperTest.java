package com.example.microservices.users.mapper;

import com.example.microservices.users.dto.FollowDTO;
import com.example.microservices.users.entity.Follow;
import com.example.microservices.users.util.FollowTestUtils.TestFollow;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestMethodOrder(value = MethodOrderer.MethodName.class)
class FollowMapperTest {

    private final FollowMapper followMapper = new FollowMapperImpl();

    @Test
    void test1_givenEntity_thenCorrect_toDTO() {
        Follow follow = new TestFollow(10L, 1L, 2L);
        FollowDTO followDTO = followMapper.toDTO(follow);
        assertEntityToDto(follow, followDTO);
    }

    @Test
    void test2_givenDTO_thenCorrect_toEntity() {
        FollowDTO followDTO = new FollowDTO(1L, 2L);
        Follow follow = followMapper.toEntity(followDTO);
        assertDTOtoEntity(followDTO, follow);
    }

    @Test
    void givenEntityList_thenCorrect_toDTOList() {
        for (int i = 0; i < 5; i++) {
            Follow follow = new TestFollow(10L + 1, 1L + i, 2L + i);
            FollowDTO followDTO = followMapper.toDTO(follow);
            assertEntityToDto(follow, followDTO);
        }
    }

    @Test
    void toEntityList() {
        for (int i = 0; i < 5; i++) {
            FollowDTO followDTO = new FollowDTO(2L + i, 1L + i);
            Follow follow = followMapper.toEntity(followDTO);
            assertDTOtoEntity(followDTO, follow);
        }
    }

    private void assertEntityToDto(Follow follow, FollowDTO followDTO) {
        assertEquals(follow.getId(), followDTO.getId());
        assertEquals(follow.getFollowingId(), followDTO.getFollowingId());
        assertEquals(follow.getFollowerId(), followDTO.getFollowerId());
        assertEquals(follow.getFollowedAt(), followDTO.getFollowedAt());
        assertNotNull(followDTO.getFollowedAt());
    }

    private void assertDTOtoEntity(FollowDTO followDTO, Follow follow) {
        assertNull(follow.getId());
        assertEquals(followDTO.getFollowingId(), follow.getFollowingId());
        assertEquals(followDTO.getFollowerId(), follow.getFollowerId());
        assertNotNull(follow.getFollowedAt());
    }
}