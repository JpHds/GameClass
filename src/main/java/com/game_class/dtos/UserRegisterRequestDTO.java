package com.game_class.dtos;

import com.game_class.models.UserType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record UserRegisterRequestDTO(String username,
                                        String email, 
                                        String password,
                                        String cpf,
                                        @Enumerated(EnumType.STRING) UserType userType) {}