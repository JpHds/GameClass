package com.game_class.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.game_class.dtos.UserLoginDTO;
import com.game_class.dtos.UserRegisterRequestDTO;
import com.game_class.dtos.UserRegisterResponseDTO;
import com.game_class.exceptions.UnableToRegisterUserException;
import com.game_class.models.User;
import com.game_class.services.AuthenticationService;
import com.game_class.services.CookieService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private CookieService cookieService;


    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDTO> register(@RequestBody UserRegisterRequestDTO user) {
        try {
            UserRegisterResponseDTO userToRegister = authenticationService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(userToRegister); 
        } catch (Exception e) {
            throw new UnableToRegisterUserException("Não foi possível registrar o usuário.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO user) {
        try {
            UserLoginDTO userToLogin = authenticationService.login(user);
            return cookieService.createTokenCookie(new User(user.username(), user.password()));
        } catch (Exception e) {
            throw new RuntimeException("Usuário ou senha inválidos.");
        }
    }
}
