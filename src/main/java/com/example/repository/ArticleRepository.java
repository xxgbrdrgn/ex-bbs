package com.example.repository;

import com.example.domain.Article;
import com.example.domain.ArticleExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
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
                order by id desc
                """;
        return template.query(sql, ARTICLE_ROW_MAPPER);
    }

    public List<Article> findAllArticlesAndComments() {
        ArticleExtractor articleExtractor = new ArticleExtractor();
        String sql = """
                 select a.id as article_id
                 , a.name as article_name
                 , a.content as article_content
                 , c.id as comment_id
                 , c.name as comment_name
                 , c.content as comment_content
                 from articles as a
                left outer join comments as c
                 on a.id = c.article_id
                 order by a.id desc;
                
                """;
        return template.query(sql, articleExtractor);
    }

    /**
     * articlesテーブルからid指定でデータを取得する.
     *
     * @param id 記事ID
     * @return 条件を満たす記事
     */
    public Article findById(Integer id) {
        String sql = """
                select id, name, content
                from articles
                where id = :id
                """;
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        return template.queryForObject(sql, param, ARTICLE_ROW_MAPPER);
    }

    /**
     * 新しい投稿をarticlesテーブルに追加する.
     *
     * @param article 新しい投稿
     */
    public void insert(Article article) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(article);
        if (article.getId() == null) {
            String insertSql = """
                    insert into articles(name, content)
                    values(:name, :content)
                    """;
            template.update(insertSql, param);
        }
    }

    /**
     * articlesテーブルからデータを削除する.
     *
     * @param articleId 削除する投稿のID
     */
    public void deleteByArticleId(Integer articleId) {
        String deleteSql = """
                WITH deleted_comments AS
                (DELETE FROM comments WHERE article_id = :articleId)
                DELETE FROM articles WHERE id = :articleId;
                """;
        SqlParameterSource param = new MapSqlParameterSource().addValue("articleId", articleId);
        template.update(deleteSql, param);

    }
}
