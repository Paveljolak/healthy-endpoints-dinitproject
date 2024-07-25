package com.pavel.dinit.project.dtos;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class EditUserDto {

    private String username;
    private String password;
}
