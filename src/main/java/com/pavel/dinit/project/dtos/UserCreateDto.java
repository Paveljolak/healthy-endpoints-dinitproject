package com.pavel.dinit.project.dtos;


import com.pavel.dinit.project.models.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserCreateDto {

    private String username;

    private String password;

    private String email;

    private String role;



    public static User createDtoToUser(UserCreateDto dto){
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        return user;
    }



    public static UserCreateDto userCreateDtoFromUser (User user){
        return new UserCreateDto(
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRole()
        );
    }

}
