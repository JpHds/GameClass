package com.game_class.dtos;

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
    private Long commentsCount;

}
