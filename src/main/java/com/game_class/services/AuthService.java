package com.game_class.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.game_class.dtos.UserLoginRequestDTO;
import com.game_class.dtos.UserRegisterRequestDTO;
import com.game_class.exceptions.DataAlreadyInUseException;
import com.game_class.exceptions.InvalidCredentialsException;
import com.game_class.models.User;
import com.game_class.repositories.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User register(UserRegisterRequestDTO user) {
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

        return newUser;
    }

    public User login(UserLoginRequestDTO user) {

        User userToLogin = userRepository.findByUsername(user.username());

        if (userToLogin == null){
            throw new InvalidCredentialsException("Usuário não encontrado.");}

        if (!passwordEncoder.matches(user.password(), userToLogin.getPassword()))
            throw new InvalidCredentialsException("Senha incorreta.");


        return userToLogin;
    }
}