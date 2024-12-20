package com.game_class.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.game_class.models.User;
import com.game_class.security.TokenService;

@Service
public class CookieService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Value("${LOGIN_VALIDATE_HOURS}")
    private Long validate;

    public ResponseEntity<String> createTokenCookie(User user) {

        var userPassword = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        authenticationManager.authenticate(userPassword);
        var token = tokenService.generateToken(user);

        ResponseCookie tokenCookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .maxAge(validateTime())
                .path("/")
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, tokenCookie.toString())
                .body("Login realizado!");
    }

    public Long validateTime() {
        return validate * 60 * 60;
    }
}
