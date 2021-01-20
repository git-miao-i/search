package com.miao.service;

import com.miao.entity.Article;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author MiaoChuanXin
 */
public interface SearchService {

    public List search() throws IOException;

    public List<Article> searchA(Article article);
}
