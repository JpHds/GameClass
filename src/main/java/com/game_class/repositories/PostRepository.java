package com.game_class.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.game_class.dtos.PostWithCommentsCountDTO;
import com.game_class.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT new com.game_class.dtos.PostWithCommentsCountDTO(" +
            "p.postId, u.username, p.postQuestion, COUNT(DISTINCT c.commentId)) AS commentsCount " +
            "FROM Post p " +
            "LEFT JOIN p.user u " +
            "LEFT JOIN Comment c ON c.post = p " +
            "GROUP BY p.postId, u.username, p.postQuestion")
    List<PostWithCommentsCountDTO> findAllWithCommentsCount();

    @Query("SELECT new com.game_class.dtos.PostWithCommentsCountDTO(" +
            "p.postId, u.username, p.postQuestion, COUNT(DISTINCT c.commentId)) AS commentsCount " +
            "FROM Post p " +
            "LEFT JOIN p.user u " +
            "LEFT JOIN Comment c ON c.post = p " +
            "LEFT JOIN p.postMatter m " +
            "WHERE m.matterId = :matterId " +
            "GROUP BY p.postId, u.username, p.postQuestion")
    List<PostWithCommentsCountDTO> findPostsByMatterId(@Param("matterId") Long matterId);
}
