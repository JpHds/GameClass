package com.game_class.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.game_class.dtos.UserLoginRequestDTO;
import com.game_class.dtos.UserLoginResponseDTO;
import com.game_class.dtos.UserRegisterRequestDTO;
import com.game_class.dtos.UserRegisterResponseDTO;
import com.game_class.exceptions.UnableToRegisterUserException;
import com.game_class.services.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDTO> register(@Validated @RequestBody UserRegisterRequestDTO user) {
        System.out.println("Entrou no controller");

        System.out.println(user);
        try {
            UserRegisterResponseDTO userToRegister = authService.register(user);
            return ResponseEntity.ok(userToRegister);
        } catch (Exception e) {
            throw new UnableToRegisterUserException("Não foi possível registrar o usuário.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@Validated @RequestBody UserLoginRequestDTO user) {
        try {
            UserLoginResponseDTO userToLogin = authService.login(user);
            return ResponseEntity.ok(userToLogin);
        } catch (Exception e) {
            throw new RuntimeException("Usuário ou senha inválidos.");
        }
    }
}
