package com.game_class.dtos;

import com.game_class.models.UserType;

public record UserRegisterRequestDTO(String username, String email, String password, UserType userType) {}