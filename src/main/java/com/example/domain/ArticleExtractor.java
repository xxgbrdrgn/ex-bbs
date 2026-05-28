package com.example.domain;

import lombok.Data;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
public class ArticleExtractor implements ResultSetExtractor<List<Article>> {
    @Override
    public List<Article> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Article> articleList = new ArrayList<>();
        int idChecker = -1;

        while (rs.next()) {

            if (rs.getInt("article_id") != idChecker) {
                Article article = new Article();
                article.setId(rs.getInt("article_id"));
                article.setName(rs.getString("article_name"));
                article.setContent(rs.getString("article_content"));
                if (rs.getString("comment_id") != null) {
                    Comment comment = new Comment();
                    comment.setId(rs.getInt("comment_id"));
                    comment.setName(rs.getString("comment_name"));
                    comment.setContent(rs.getString("comment_content"));
                    comment.setArticleId(rs.getInt("article_id"));
                    article.commentList = new ArrayList<>();
                    article.commentList.add(comment);
                }
                articleList.add(article);
                idChecker = article.getId();
            } else {
                if (rs.getString("comment_id") != null) {
                    Comment comment = new Comment();
                    comment.setId(rs.getInt("comment_id"));
                    comment.setName(rs.getString("comment_name"));
                    comment.setContent(rs.getString("comment_content"));
                    comment.setArticleId(rs.getInt("article_id"));
                    articleList.getLast().commentList.add(comment);
                }
            }
        }
        return articleList;
    }
}
