package com.example.repository;

import com.example.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * commentsテーブルを操作するリポジトリ.
 */
@Repository
public class CommentRepository {
    @Autowired
    NamedParameterJdbcTemplate template;

    private static final RowMapper<Comment> COMMENT_ROW_MAPPER = (rs, i) -> {
        Comment comment = new Comment();
        comment.setId(rs.getInt("id"));
        comment.setName(rs.getString("name"));
        comment.setContent(rs.getString("content"));
        comment.setArticleId(rs.getInt("article_id"));
        return comment;
    };

    /**
     * 記事IDからコメントのリストを取得する.
     *
     * @param articleId 記事ID
     * @return コメントリスト
     */
    public List<Comment> findByArticleId(int articleId) {
        String sql = """
                select id, name, content, article_id
                from comments
                where article_id = :articleId
                order by id desc
                """;
        SqlParameterSource param = new MapSqlParameterSource().addValue("articleId", articleId);
        return template.query(sql, param, COMMENT_ROW_MAPPER);
    }

    public void save(Comment comment) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(comment);
        if (comment.getId() == null) {
            String insertSql = """
                    insert into comment(name, content)
                    values(:name, :content)
                    """;
            template.update(insertSql, param);
        }
    }
}
