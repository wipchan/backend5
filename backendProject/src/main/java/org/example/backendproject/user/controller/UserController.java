package org.example.backendproject.user.controller;


import lombok.RequiredArgsConstructor;
import org.example.backendproject.user.dto.UserDTO;
import org.example.backendproject.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

//    @Value("${PROJECT_NAME:web Server}")
//    private String instansName;
//
//    @GetMapping
//    public String test(){
//        return instansName;
//    }
    private final UserService userService;

    /*내 정보 보기*/
    @GetMapping("/me/{id}")
    public ResponseEntity<UserDTO> getMyInfo(@PathVariable("id") Long userid){
        return ResponseEntity.ok(userService.getMyInfo(userid));
    }

    /*유저 정보 수정*/
    @PutMapping("/me/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") Long userid, @RequestBody UserDTO dto){
        UserDTO updated = userService.updateUser(userid, dto);
        return ResponseEntity.ok(updated);
    }
}
