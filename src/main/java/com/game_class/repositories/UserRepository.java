package com.game_class.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.game_class.dtos.UsersRankingDTO;
import com.game_class.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailOrUsername(String email, String username);

    User findByUsername(String username);

    @Query("SELECT new com.game_class.dtos.UsersRankingDTO(" +
           "u.username, SUM(v.voteValue)) " +
           "FROM User u " +
           "JOIN Comment c ON c.user = u " +
           "JOIN Vote v ON v.comment = c " +
           "GROUP BY u.userId, u.username " +
           "ORDER BY SUM(v.voteValue) DESC")
    List<UsersRankingDTO> findUsersRank();
}
