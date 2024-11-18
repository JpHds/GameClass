package com.game_class.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.game_class.models.User;
import com.game_class.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public UserLoginResponseDTO login(@Validated @RequestBody UserLoginRequestDTO user) {
        try {
            User userToLogin = authService.login(user);
            return new UserLoginResponseDTO(
                    userToLogin.getUsername(),
                    userToLogin.getEmail()
                    );
        } catch (Exception e) {
            throw new RuntimeException("Usuário ou senha inválidos.");
        }
    }

    @PostMapping("/register")
    public UserRegisterResponseDTO register(@Validated @RequestBody UserRegisterRequestDTO user) {
        try {
            User userToRegister = authService.register(user);
            return new UserRegisterResponseDTO(
                    userToRegister.getUserId(),
                    userToRegister.getUsername(),
                    userToRegister.getEmail(),
                    userToRegister.getUserType());
        } catch (Exception e) {
            throw new UnableToRegisterUserException("Não foi possível registrar o usuário.");
        }
    }
}
