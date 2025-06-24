package org.example.backendproject.Auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.backendproject.user.entity.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity // DB 테이블과 자바 객체를 연결
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tokenType;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;

    @OneToOne(fetch = FetchType.LAZY) // 지연로딩 적용 -> Auth 엔티티 조회할때 user 객체는 불러오지 않음
    @JoinColumn(name = "user_id") // auth.getUser()에 실제로 접근할 때 User 쿼리 발생!
    private User user;
}
