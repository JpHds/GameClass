package com.game_class.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.game_class.models.User;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByEmail(String email);
    User findByUsername(String username);
}
