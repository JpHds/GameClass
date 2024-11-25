package com.game_class.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.game_class.dtos.VoteDTO;
import com.game_class.models.User;
import com.game_class.repositories.VoteRepository;
import com.game_class.services.AuthenticationService;

@RestController
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private VoteRepository voteRepository;

    public ResponseEntity<?> upsertVote(@RequestBody VoteDTO vote) {
        User user = (User) this.authenticationService.getCurrentAuthentication().getPrincipal();
        voteRepository.upsertVote(user.getUserId(), vote.postId(), vote.voteValue());

        return ResponseEntity.ok().build();
    }
}
