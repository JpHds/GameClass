package com.game_class.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.game_class.dtos.CommentDTO;
import com.game_class.models.Comment;

import jakarta.transaction.Transactional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT new com.game_class.dtos.CommentDTO(c.commentId, c.textComment, u.username, " +
            "v.voteValue) " +
            "FROM Comment c " +
            "LEFT JOIN c.post p " +
            "LEFT JOIN c.user u " +
            "LEFT JOIN Vote v ON v.comment.commentId = c.commentId AND v.user.userId = :userId " +
            "WHERE p.postId = :postId")
    List<CommentDTO> getCommentsByPostId(
            @Param("postId") Long postId,
            @Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Comment " +
            "SET voteCount = :votesAmount " +
            "WHERE commentId = :commentId")
    int upsertVoteCount(@Param("votesAmount") int votesAmount, @Param("commentId") Long commentId);
}
