package com.example.domain;

import lombok.*;

import java.util.List;

/**
 * 記事を表すドメイン.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Article {
    /**
     * ID
     */
    private Integer id;
    /**
     * 投稿者名
     */
    private String name;
    /**
     * 投稿内容
     */
    private String content;
    /**
     * 投稿に対するコメント
     */
    private List<Comment> commentList;
}
