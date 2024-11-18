package com.game_class.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.game_class.dtos.UserLoginRequestDTO;
import com.game_class.dtos.UserLoginResponseDTO;
import com.game_class.dtos.UserRegisterRequestDTO;
import com.game_class.dtos.UserRegisterResponseDTO;
import com.game_class.exceptions.DataAlreadyInUseException;
import com.game_class.exceptions.InvalidCredentialsException;
import com.game_class.models.User;
import com.game_class.repositories.UserRepository;
import com.game_class.security.TokenService;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CookieService cookieService;

    public UserRegisterResponseDTO register(UserRegisterRequestDTO user) {
        User userToRegister = userRepository.findByEmail(user.username());

        if (userToRegister != null) {
            if (user.email().equals(userToRegister.getEmail())) {
                throw new DataAlreadyInUseException("Email " + user.email() + " já está em uso.");
            }

            if (user.username().equals(userToRegister.getUsername())) {
                throw new DataAlreadyInUseException("Nome de usuário " + user.username() + " já está em uso.");
            }
        }

        User newUser = new User(user.username(), user.email(), passwordEncoder.encode(user.password()),
                user.userType());
        userRepository.save(newUser);

        return new UserRegisterResponseDTO(newUser.getUserId(), newUser.getUsername(), newUser.getEmail(), newUser.getUserType());
    }

    public UserLoginResponseDTO login(UserLoginRequestDTO user) {

        User userToLogin = userRepository.findByUsername(user.username());

        if (userToLogin == null) {
            throw new InvalidCredentialsException("Usuário não encontrado.");
        }

        if (!passwordEncoder.matches(user.password(), userToLogin.getPassword()))
            throw new InvalidCredentialsException("Senha incorreta.");

        var userPassword = new UsernamePasswordAuthenticationToken(user.username(), user.password());
        var auth = this.authenticationManager.authenticate(userPassword);
        var token = tokenService.generateToken(userToLogin);
        cookieService.createTokenCookie(token);
        return new UserLoginResponseDTO(userToLogin.getUsername(), userToLogin.getEmail());
    }
}