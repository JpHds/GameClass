package com.game_class.dtos;

import com.game_class.models.UserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostWithCommentsCountDTO {

    private Long postId;
    private String username;
    private String postQuestion;
    private UserType userType;
    private Long commentsCount;

}
