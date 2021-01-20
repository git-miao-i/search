package com.miao.dao;

import com.miao.entity.Article;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.HighlightParameters;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface ArticleRepository extends ElasticsearchRepository<Article, Long> {

    @Highlight(fields = {
            @HighlightField(name = "name"),
            @HighlightField(name = "content")
    }, parameters = @HighlightParameters(preTags = "<span style = 'color:red'>", postTags = "</span>"))
    List<SearchHit<Article>> findArticlesByNameAndContent(String name, String content);

}
