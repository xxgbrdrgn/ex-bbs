package com.example.controller;

import com.example.domain.Article;
import com.example.domain.ArticleForm;
import com.example.domain.Comment;
import com.example.domain.CommentForm;
import com.example.repository.ArticleRepository;
import com.example.repository.CommentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String index(Model model, ArticleForm articleForm, CommentForm commentForm) {
        List<Article> articleList = articleRepository.findAllArticlesAndComments();
        model.addAttribute("articleList", articleList);
        return "bbs";
    }

    /**
     * 記事を投稿する.
     *
     * @param model         モデル
     * @param articleForm   記事フォーム
     * @param articleResult 記事エラーリスト
     * @param commentForm   コメントフォーム
     * @return 記事画面
     */
    @PostMapping("insert-article")
    public String insertArticle(Model model, @Validated ArticleForm articleForm, BindingResult articleResult, CommentForm commentForm) {
        if (articleResult.hasErrors()) {
            return index(model, articleForm, commentForm);
        }
        Article article = Article.builder()
                .name(articleForm.getName())
                .content(articleForm.getContent())
                .build();
        articleRepository.insert(article);
        return "redirect:/bbs";
    }

    /**
     * コメントを投稿する.
     *
     * @param articleForm   投稿フォーム
     * @param commentForm   コメントフォーム
     * @param commentResult コメントエラーリスト
     * @param model         モデル
     * @return 記事一覧
     */
    @PostMapping("insert-comment")
    public String insertComment(ArticleForm articleForm, @Validated CommentForm commentForm, BindingResult commentResult, Model model) {
        if (commentResult.hasErrors()) {
            model.addAttribute("errorArticleId", Integer.valueOf(commentForm.getArticleId()));
            return index(model, articleForm, commentForm);
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentForm, comment);
        comment.setArticleId(Integer.valueOf(commentForm.getArticleId()));
        commentRepository.insert(comment);
        return "redirect:/bbs";
    }

    /**
     * 記事を削除する.
     *
     * @param model モデル
     * @param id    ID
     * @return 記事一覧画面
     */
    @PostMapping("delete-article")
    public String deleteArticle(Model model, String id) {
        articleRepository.deleteByArticleId(Integer.parseInt(id));
        return "redirect:/bbs";
    }
}
