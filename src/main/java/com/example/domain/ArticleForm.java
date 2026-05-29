package com.example.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleForm {
    /**
     * 投稿者名
     */
    @Size(max = 50, message = "50文字以内で入力してください")
    @NotBlank(message = "入力必須です")
    private String name;
    /**
     * 投稿内容
     */
    @NotBlank(message = "入力必須です")
    private String content;
}
