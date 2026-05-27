package com.example.domain;

import lombok.*;

/**
 * コメントを表すドメイン.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Comment {
    /**
     * ID
     */
    private Integer id;
    /**
     * コメント者名
     */
    private String name;
    /**
     * コメント内容
     */
    private String content;
    /**
     * コメント先の記事ID
     */
    private Integer articleId;
}
