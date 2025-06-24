package org.example.backendproject.Auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.backendproject.Auth.dto.LoginRequestDTO;
import org.example.backendproject.Auth.dto.SignUpRequestDTO;
import org.example.backendproject.user.dto.UserDTO;
import org.example.backendproject.Auth.service.AuthService;
import org.example.backendproject.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;

    /** 회원가입 **/
    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO){
        try {
            authService.signUp(signUpRequestDTO);
            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e){
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /** 로그인 **/
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        try {
            authService.login(loginRequestDTO);
            UserDTO loginUser = authService.login(loginRequestDTO);
            System.out.println("로그인 성공 = "+ new ObjectMapper().writeValueAsString(loginUser));
            return ResponseEntity.ok(loginUser);
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

}
