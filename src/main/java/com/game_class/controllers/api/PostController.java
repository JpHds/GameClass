package com.game_class.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.game_class.dtos.PostDTO;
import com.game_class.dtos.PostWithCommentsCountDTO;
import com.game_class.models.Matter;
import com.game_class.models.Post;
import com.game_class.models.User;
import com.game_class.repositories.MatterRepository;
import com.game_class.repositories.PostRepository;
import com.game_class.services.AuthenticationService;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private MatterRepository matterRepository;

    @PostMapping("/new")
    public ResponseEntity<?> createNewPost(@RequestBody PostDTO post) {
        User user = (User) this.authenticationService.getCurrentAuthentication().getPrincipal();

        Matter matter = matterRepository.findByMatterId(post.matterId());

        postRepository.save(new Post(post.question(), matter, user));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public List<PostWithCommentsCountDTO> getAllPosts() {
        List<PostWithCommentsCountDTO> postsList = postRepository.findAllPostsWithCommentsCount();
        return postsList;
    }

    @GetMapping("/matter/{matterId}")
    public List<PostWithCommentsCountDTO> getPostsByMatter(@PathVariable("matterId") Long matterId) {
        if (matterId != 0) {
            return postRepository.findPostsWithCommentsCountByMatterId(matterId);
        } else {
            return postRepository.findAllPostsWithCommentsCount();
        }
    }

    @GetMapping("/matter/{matterId}/my")
    public List<PostWithCommentsCountDTO> getPostsByMatterAndSessionUser(@PathVariable("matterId") Long matterId,
            @AuthenticationPrincipal User user) {
        if (matterId != 0) {
            return postRepository.findPostsWithCommentsCountByMatterIdAndUserSession(matterId, user.getUserId());
        } else {
            return postRepository.findAllPostsWithCommentsCount();
        }
    }

    @GetMapping("/{postId}")
    public PostWithCommentsCountDTO getPostById(@PathVariable("postId") Long postId) {
        return postRepository.findPostById(postId);
    }

    @GetMapping("/myPublishes")
    public List<PostWithCommentsCountDTO> getPostsBySessionUserId() {
        User user = (User) this.authenticationService.getCurrentAuthentication().getPrincipal();
        return postRepository.findPostsByUserId(user.getUserId());
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        try {
            postRepository.deleteById(postId);
            return ResponseEntity.noContent().build(); 
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}