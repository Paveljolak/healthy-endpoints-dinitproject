package com.pavel.dinit.project.dtos;

import com.pavel.dinit.project.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
    private String username;
    private String password;
    private String email;

    public static UserRegisterDto fromUser(User user) {
        return new UserRegisterDto(
                user.getUsername(),
                user.getPassword(),
                user.getEmail()
        );
    }
}
