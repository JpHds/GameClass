package com.game_class.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.game_class.dtos.CommentDTO;
import com.game_class.dtos.CommentResponseDTO;
import com.game_class.dtos.NewCommentDTO;
import com.game_class.models.Comment;
import com.game_class.models.Post;
import com.game_class.models.User;
import com.game_class.repositories.CommentRepository;
import com.game_class.repositories.PostRepository;
import com.game_class.repositories.UserRepository;
import com.game_class.services.AuthenticationService;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

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

    @PostMapping("/new")
    public ResponseEntity<?> createComment(@RequestBody NewCommentDTO comment, @AuthenticationPrincipal User userAuthenticated) {
        Post post = postRepository.findById(comment.postId()).orElse(null);
        User user = userRepository.findById(userAuthenticated.getUserId()).orElse(null);
        Comment newComment = new Comment(comment.comment(), post, user);
        commentRepository.save(newComment);

        CommentResponseDTO response = new CommentResponseDTO(newComment.getCommentId(), newComment.getTextComment(), user.getUsername());
        return ResponseEntity.ok(response);
    }

}
