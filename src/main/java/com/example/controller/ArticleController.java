package com.example.controller;

import com.example.domain.Article;
import com.example.domain.Comment;
import com.example.repository.ArticleRepository;
import com.example.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 記事情報を操作するコントローラー.
 */
@Controller
@RequestMapping("/bbs")
public class ArticleController {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    CommentRepository commentRepository;

    /**
     * 記事一覧を表示する.
     *
     * @param model モデル
     * @return 記事一覧画面
     */
    @GetMapping("")
    public String index(Model model) {
        List<Article> articleList = new ArrayList<>();
        articleList = articleRepository.findAll();
        for (Article article : articleList) {
            article.setCommentList(commentRepository.findByArticleId(article.getId()));
        }
        model.addAttribute("articleList", articleList);
        return "bbs";
    }

    /**
     * 記事をポストする.
     *
     * @param model   モデル
     * @param name    ポストする投稿者名
     * @param content ポストする投稿内容
     * @return 記事一覧画面
     */
    @PostMapping("post-article")
    public String postArticle(Model model, String name, String content) {
        Article article = Article.builder()
                .name(name)
                .content(content)
                .build();
        articleRepository.save(article);
        return "redirect:/bbs";
    }

    @PostMapping("post-comment")
    public String postComment(Model model, String name, String content, int articleId) {
        Comment comment = Comment.builder()
                .name(name)
                .content(content)
                .articleId(articleId)
                .build();
        commentRepository.save(comment);
        System.out.println(comment.toString());
        return "redirect:/bbs";
    }
}
