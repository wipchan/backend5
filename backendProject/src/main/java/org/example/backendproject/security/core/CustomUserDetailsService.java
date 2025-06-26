package org.example.backendproject.security.core;


import lombok.RequiredArgsConstructor;
import org.example.backendproject.user.entity.User;
import org.example.backendproject.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 로그인할 때 스프링에서 DB에 현재 로그인 하는 사용자가 존재하는지 확인하는 메서드
    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        User user = userRepository.findByUserid(userid).orElseThrow(
                () -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다 -> "+ userid));
        return new CustomUserDetails(user);
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다 -> "+ id));
        return new CustomUserDetails(user);
    }
}
