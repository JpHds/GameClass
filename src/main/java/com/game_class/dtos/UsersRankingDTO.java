package com.game_class.dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersRankingDTO {

    private String username;
    private Long sumVotes;

}