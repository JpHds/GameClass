package com.game_class.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.game_class.dtos.UsersRankingDTO;
import com.game_class.repositories.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;
    
    @GetMapping("/rank")
    public ResponseEntity<?> getUsersRank() {
        List<UsersRankingDTO> users = userRepository.findUsersRank();
        return ResponseEntity.ok(users);
    }
}
