package com.example.controller;

import com.example.domain.Article;
import com.example.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/bbs")
public class ArticleController {
    @Autowired
    ArticleRepository articleRepository;

    @GetMapping("")
    public String index(Model model) {
        List<Article> articleList = new ArrayList<>();
        articleList = articleRepository.findAll();
        model.addAttribute("articleList", articleList);

        return "bbs";
    }

    @PostMapping("post-article")
    public String postArticle(Model model, String name, String content) {
        Article article = Article.builder()
                .name(name)
                .content(content)
                .build();
        Integer insertedId = articleRepository.save(article);
        Article insertedArticle = articleRepository.findById(insertedId);
        model.addAttribute(insertedArticle);
        return "bbs";
    }
}
