package com.miao.service;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * @author MiaoChuanXin
 */
public interface SearchService {

    public List search() throws IOException;
}
