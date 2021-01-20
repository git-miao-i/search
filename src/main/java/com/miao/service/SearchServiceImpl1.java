package com.miao.service;

import com.miao.entity.Article;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author MiaoChuanXin
 */
@Service
public class SearchServiceImpl1 implements SearchService {
    @Override
    public List search() throws IOException {
        return null;
    }

    @Override
    public List<Article> searchA(Article article) {
        return null;
    }
}
