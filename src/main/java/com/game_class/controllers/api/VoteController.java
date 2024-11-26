package com.game_class.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.game_class.dtos.VoteDTO;
import com.game_class.models.User;
import com.game_class.repositories.CommentRepository;
import com.game_class.repositories.VoteRepository;
import com.game_class.services.AuthenticationService;

@RestController
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("/upsert")
    public ResponseEntity<?> upsertVote(@RequestBody VoteDTO vote) {
        try {
            User user = (User) this.authenticationService.getCurrentAuthentication().getPrincipal();
            voteRepository.upsertVote(user.getUserId(), vote.commentId(), vote.voteValue());
            int votesAmount = voteRepository.getCountVotes(vote.commentId());
            commentRepository.upsertVoteCount(votesAmount, vote.commentId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
