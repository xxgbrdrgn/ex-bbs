package com.example.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentForm {
    /**
     * コメント者名
     */
    @NotBlank(message = "入力必須です")
    @Size(max = 50, message = "50文字以内で入力してください")
    private String name;
    /**
     * コメント内容
     */
    @NotBlank(message = "入力必須です")
    private String content;

    /**
     * コメント先投稿のID
     */
    private String articleId;
}
