package com.miao.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * @author MiaoChuanXin
 */
@Data
@Document(indexName = "article")
public class Article {

    private @Id long id;
    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_smart", type = FieldType.Text)
    private String name;
    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_smart", type = FieldType.Text)
    private String content;
    private String keyword;

}
