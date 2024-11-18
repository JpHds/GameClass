package com.game_class.dtos;

import com.game_class.models.UserType;

public record UserRegisterResponseDTO(Long userId, String userName, String email, UserType userType) {}
