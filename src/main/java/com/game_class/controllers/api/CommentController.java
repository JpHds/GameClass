package com.game_class.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.game_class.dtos.CommentDTO;
import com.game_class.models.User;
import com.game_class.repositories.CommentRepository;
import com.game_class.services.AuthenticationService;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable("postId") Long postId) {
        User user = (User) this.authenticationService.getCurrentAuthentication().getPrincipal();

        List<CommentDTO> comments = commentRepository.getCommentsByPostId(postId, user.getUserId());

        if (comments == null || comments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(comments);
    }

}
