package org.example.backendproject.user.controller;


import lombok.RequiredArgsConstructor;
import org.example.backendproject.security.core.CustomUserDetails;
import org.example.backendproject.user.dto.UserDTO;
import org.example.backendproject.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails){
        Long id = userDetails.getId();
        return ResponseEntity.ok(userService.getMyInfo(id));
    }

    /*유저 정보 수정*/
    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateUser(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UserDTO userDTO){
        Long id = userDetails.getId();
        UserDTO updated = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updated);
    }

    //아래는 순환참조가 되는  예제
//    @GetMapping("/profile/{profileId}")
//    public User getProfile2(@PathVariable Long profileId)  {
//        return userService.getProfile2(profileId);
//    }

    //dto로 순환참조 방지
//    @GetMapping("/profile/{profileId}")
//    public UserDTO getProfile(@PathVariable Long profileId)  {
//        return userService.getProfile(profileId);
//    }
//
//
//    @PostMapping("/jpaSaveAll")
//    public String saveAll(@RequestBody List<User> users) {
//        userService.saveAllUsers(users);
//        return "ok";
//    }
}
