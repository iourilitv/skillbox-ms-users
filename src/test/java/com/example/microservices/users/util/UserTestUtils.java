package com.example.microservices.users.util;

import com.example.microservices.users.dto.CityDTO;
import com.example.microservices.users.dto.UserDTO;
import com.example.microservices.users.entity.City;
import com.example.microservices.users.entity.Follow;
import com.example.microservices.users.entity.User;
import com.example.microservices.users.enums.Gender;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.microservices.users.util.FollowTestUtils.createFollowSetForUsers;

public class UserTestUtils {

    public static void fillUpUsers(int size, List<User> users, List<City> cities) {
        for (int i = 0; i < size; i++) {
            users.add(createUser(i, cities.get(i % cities.size())));
        }
    }

    public static void fillUpTestUsers(int size, List<User> testUsers) {
        for (int i = 0; i < size; i++) {
            testUsers.add(createUserWithId(i + 1));
        }
        Set<Follow> followSet = createFollowSetForUsers(testUsers);
        setFollowings(testUsers, followSet);
        setFollowers(testUsers, followSet);
    }

    private static void setFollowings(List<User> testUsers, Set<Follow> followSet) {
        testUsers.forEach(user -> user.setFollowings(followSet.stream()
                .filter(follow -> Objects.equals(user.getId(), follow.getFollowerId()))
                .collect(Collectors.toSet())));
    }

    private static void setFollowers(List<User> testUsers, Set<Follow> followSet) {
        testUsers.forEach(user -> user.setFollowers(followSet.stream()
                .filter(follow -> Objects.equals(user.getId(), follow.getFollowingId()))
                .collect(Collectors.toSet())));
    }

    public static User createUserWithId(int index) {
        User user = new User(10L + index, "my_nick_name" + index);
        user.setFirstName("test_firstName" + index);
        user.setLastName("test_lastName" + index);
        user.setSecondName("test_secondName" + index);
        user.setGender(Gender.values()[index % Gender.values().length]);
        user.setBirthday(new Date());
        user.setCurrentCity(new City(100 + index, "test_cityName" + 100 + index));
        user.setEmail(index + "testEmail@mail.com");
        user.setPhone("+7(999) 999-" + StringUtils.leftPad(String.valueOf(index), 4, "0"));
        user.setAbout("Test About" + index);
        user.setHardSkills("Test Hard Skills" + index);
        return user;
    }

    public static User createUser(int index, City currentCity) {
        User user = new User("test_firstName" + index, "test_lastName" + index, Gender.values()[index % Gender.values().length],
                new Date(), currentCity, "my_nick_name" + index);
        user.setSecondName("test_secondName" + index);
        user.setEmail(index + "testEmail@mail.com");
        user.setPhone("+7(999) 999-" + StringUtils.leftPad(String.valueOf(index), 4, "0"));
        user.setAbout("Test About" + index);
        user.setHardSkills("Test Hard Skills" + index);
        return user;
    }

    public static UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO(user.getFirstName(), user.getLastName(), user.getGender(), user.getBirthday(),
                new CityDTO(user.getCurrentCity().getId(), user.getCurrentCity().getName()), user.getNickname());
        userDTO.setId(user.getId());
        userDTO.setSecondName(user.getSecondName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setAbout(user.getAbout());
        userDTO.setHardSkills(user.getHardSkills());
        userDTO.setFollowingsNumber(user.getFollowings() == null ? 0 : user.getFollowings().size());
        userDTO.setFollowersNumber(user.getFollowers() == null ? 0 : user.getFollowers().size());
        return userDTO;
    }
}