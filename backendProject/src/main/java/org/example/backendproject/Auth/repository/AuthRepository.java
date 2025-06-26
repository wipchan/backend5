package org.example.backendproject.Auth.repository;

import org.example.backendproject.Auth.entity.Auth;
import org.example.backendproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {

    //
    boolean existsByUser(User user);

    // refresh 토큰이 있는지 확인하는 쿼리 메서드
    Optional<Auth> findByRefreshToken(String refreshToken);
}
