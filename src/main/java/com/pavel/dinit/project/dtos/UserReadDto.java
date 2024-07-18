package com.pavel.dinit.project.dtos;


import com.pavel.dinit.project.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.logging.Logger;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserReadDto {

    private static final Logger logger = Logger.getLogger(UserReadDto.class.getName());

    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;


    public static UserReadDto readingDtoFromUser(User user) {
        return new UserReadDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRole()
        );
    }


    public static User readingUserFromDto(UserReadDto userReadDto) {
        User user = new User();
        user.setId(userReadDto.getId());
        user.setUsername(userReadDto.getUsername());
        user.setPassword(userReadDto.getPassword());
        user.setEmail(userReadDto.getEmail());
        user.setRole(userReadDto.getRole());
        return user;
    }


}
