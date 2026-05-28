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
        List<Article> articleList = articleRepository.findAllArticlesAndComments();
//        for (Article article : articleList) {
//            article.setCommentList(commentRepository.findByArticleId(article.getId()));
//        }
        model.addAttribute("articleList", articleList);
        System.out.println(articleList.toString());
        return "bbs";
    }

    /**
     * 記事を投稿する.
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
        articleRepository.insert(article);
        return "redirect:/bbs";
    }

    /**
     * コメントを投稿する.
     *
     * @param model     モデル
     * @param name      コメント者名
     * @param content   コメント内容
     * @param articleId コメント先の投稿ID
     * @return 記事一覧
     */
    @PostMapping("post-comment")
    public String postComment(Model model, String name, String content, int articleId) {
        Comment comment = Comment.builder()
                .name(name)
                .content(content)
                .articleId(articleId)
                .build();
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
        commentRepository.deleteByArticleId(Integer.valueOf(id));
        articleRepository.deleteById(Integer.parseInt(id));
        return "redirect:/bbs";
    }
}
