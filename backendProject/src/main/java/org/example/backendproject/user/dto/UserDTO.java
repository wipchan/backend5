package org.example.backendproject.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String userid;

    private UserProfileDTO profile;
//    아래 4개의 속성을 DTO 에서 가져오도록 분리
//    private String username;
//    private String email;
//    private String phone;
//    private String address;
}
