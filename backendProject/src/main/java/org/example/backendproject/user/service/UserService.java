package org.example.backendproject.user.service;

import lombok.RequiredArgsConstructor;
import org.example.backendproject.user.dto.UserDTO;
import org.example.backendproject.user.dto.UserProfileDTO;
import org.example.backendproject.user.entity.User;
import org.example.backendproject.user.entity.UserProfile;
import org.example.backendproject.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
//    private final UserProfileRepository userProfileRepository;

    /*내 정보 조회*/
    @Transactional(readOnly = true)
    public UserDTO getMyInfo(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. "));

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUserid(user.getUserid());

        UserProfile profile = user.getUserProfile();

        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setUsername(profile.getUsername());
        profileDTO.setEmail(profile.getEmail());
        profileDTO.setPhone(profile.getPhone());
        profileDTO.setAddress(profile.getAddress());
        dto.setProfile(profileDTO);

        return dto;
    }

    /** 유저 정보 수정 **/
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO){
        User user = userRepository.findById(id) // 유저 레포지토리를 통해서 유저를 가져옴
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        UserProfile profile = user.getUserProfile();

        if (profile != null && userDTO.getProfile() != null){
            UserProfileDTO dtoProfile = userDTO.getProfile();

            if(dtoProfile.getUsername() != null) profile.setUsername(dtoProfile.getUsername());
            if(dtoProfile.getEmail() != null) profile.setEmail(dtoProfile.getEmail());
            if(dtoProfile.getPhone() != null) profile.setPhone(dtoProfile.getPhone());
            if(dtoProfile.getAddress() != null) profile.setAddress(dtoProfile.getAddress());

        }
        //아래는 변경된 내용을 프론트에 던져주기 위해 생성합니다.
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUserid(user.getUserid());
        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setUsername(profile.getUsername());
        profileDTO.setEmail(profile.getEmail());
        profileDTO.setPhone(profile.getPhone());
        profileDTO.setAddress(profile.getAddress());
        dto.setProfile(profileDTO);
        return dto;
    }

    //dto 순환 참조 방지
//    public UserDTO getProfile(Long profileId){
//        UserProfile profile = userProfileRepository.findById(profileId)
//
//    }
}
