package com.game_class.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.game_class.models.Vote;

import jakarta.transaction.Transactional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO votes (user_id, comment_id, vote_value) " +
                    "VALUES (:userId, :commentId, :voteValue) " +
                    "ON DUPLICATE KEY UPDATE vote_value = VALUES(vote_value)", 
                    nativeQuery = true)
    int upsertVote(@Param("userId") Long userId, @Param("commentId") Long commentId,
            @Param("voteValue") Long voteValue);

    @Query("SELECT COUNT(v) " +
            "FROM Vote v " +
            "WHERE v.comment.id = :commentId")
    int getCountVotes(@Param("commentId") Long commentId);
}
