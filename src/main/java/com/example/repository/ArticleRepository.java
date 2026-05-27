package com.example.repository;

import com.example.domain.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * articlesテーブルを操作するリポジトリ.
 */
@Repository
public class ArticleRepository {
    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final RowMapper<Article> ARTICLE_ROW_MAPPER = (rs, i) -> {
        Article article = new Article();
        article.setId(rs.getInt("id"));
        article.setName(rs.getString("name"));
        article.setContent(rs.getString("content"));
        return article;
    };

    /**
     * 記事一覧を取得する.
     *
     * @return 全ての記事
     */
    public List<Article> findAll() {
        String sql = """
                select id, name, content
                from articles
                order by id
                """;
        return template.query(sql, ARTICLE_ROW_MAPPER);
    }

    public Integer save(Article article) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(article);
        if (article.getId() == null) {
            String insertSql = """
                    insert into articles(name, content)
                    values(:name, :content)
                    """;
            KeyHolder keyHolder = new GeneratedKeyHolder();
            String[] keyColumnNames = {"id"};
            template.update(insertSql, param, keyHolder, keyColumnNames);
            article.setId(keyHolder.getKey().intValue());
            return article.getId();
        } else {
            return null;
        }
    }

    public Article findById(Integer id) {
        String sql = """
                select id, name, content
                from articles
                where id = :id
                """;
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        return template.queryForObject(sql, param, ARTICLE_ROW_MAPPER);
    }
}
