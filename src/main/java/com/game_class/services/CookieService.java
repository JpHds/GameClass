package com.game_class.services;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    public void createTokenCookie(String token) {
        ResponseCookie tokenCookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .maxAge(hoursForSeconds(2))
                .path("/")
                .sameSite("Strict")
                .build();
        ResponseEntity<String> response = ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, tokenCookie.toString())
                .body("Cookie criado com sucesso!");
    }

    public int hoursForSeconds(int hours) {
        return hours * 60 * 60;
    }
}
