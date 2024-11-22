package com.game_class.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.game_class.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    
}
