package com.miao.service.impl;


import com.miao.dao.ArticleRepository;
import com.miao.entity.Article;
import com.miao.service.SearchService;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author MiaoChuanXin
 */
@Service
public class SearchServiceImpl implements SearchService {
    @Qualifier("elasticsearchClient")
    @Autowired
    private RestHighLevelClient client;

    @Qualifier("template")
    @Autowired
    private ElasticsearchRestTemplate template;



    @Autowired
    private ArticleRepository repository;
    @Override
    public List search() throws IOException {
        SearchRequest request = new SearchRequest("jd");
        SearchSourceBuilder builder = new SearchSourceBuilder();

        builder.query(
                QueryBuilders.matchQuery("title", "java")
                        .fuzziness(Fuzziness.AUTO)
                        .prefixLength(3)
                        .maxExpansions(10)
        );
        builder.from(0);
        builder.size(5);
        builder.sort(new FieldSortBuilder("id").order(SortOrder.ASC));

        HighlightBuilder highLight = new HighlightBuilder();
        HighlightBuilder.Field highTitle = new HighlightBuilder.Field("title");

        highTitle.highlighterType("unified");
        highLight.field(highTitle);
        builder.highlighter(highLight);

        request.source(builder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();

        //http status code
        RestStatus status = response.status();
        //执行时间
        TimeValue took = response.getTook();
        //是否提前终止
        Boolean terminatedEarly = response.isTerminatedEarly();
        //是否超时
        boolean timedOut = response.isTimedOut();
        int totalShards = response.getTotalShards();
        int successfulShards = response.getSuccessfulShards();
        int failedShards = response.getFailedShards();
        for (ShardSearchFailure failure : response.getShardFailures()) {
            // failures should be handled here
        }

        TotalHits totalHits = hits.getTotalHits();
        long value = totalHits.value;
        TotalHits.Relation relation = totalHits.relation;
        float maxScore = hits.getMaxScore();

//        SearchHit[] hitsHits = hits.getHits();
//        for (SearchHit hit : hitsHits) {
//            String index = hit.getIndex();
//            String id = hit.getId();
//            float score = hit.getScore();
//
//            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
//            String title = (String) sourceAsMap.get("title");
//
//            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
//            HighlightField highlight = highlightFields.get("title");
//            Text[] fragments = highlight.fragments();
//            String string = fragments[0].string();
//            System.out.println(string);
//
//        }


        return null;
    }
    @Override
    public List<Article> searchA(Article article){
        List<Article> linkedList = new LinkedList<>();

        List<SearchHit<Article>> list = repository.findArticlesByNameAndContent(article.getKeyword(), article.getKeyword());

        for (SearchHit<Article> searchHit : list) {
            Article newArticle = new Article();
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            List<String> name = highlightFields.get("name");
            List<String> content = highlightFields.get("content");
            StringBuilder stringBuilder = new StringBuilder();
            StringBuilder sbContent = new StringBuilder();

            for (String na : name) {
                stringBuilder.append(na);
            }
            newArticle.setName(stringBuilder.toString());

            for (String con : content) {
                sbContent.append(con);
            }
            newArticle.setContent(sbContent.toString());

            linkedList.add(newArticle);
            // TODO: 2021/1/4
        }

        return linkedList;
    }

    public List<Article> searchPlus(Article article){
        Pageable pageable = PageRequest.of(0, 100);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightContent = new HighlightBuilder.Field("content");
        HighlightBuilder.Field highlightName = new HighlightBuilder.Field("name");
        highlightBuilder.field(highlightContent);
        highlightBuilder.field(highlightName);
        //highlightBuilder.requireFieldMatch(true);
        highlightBuilder.preTags("<span style = 'color:red'>");
        highlightBuilder.postTags("</span>");

        Query query = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withHighlightBuilder(highlightBuilder)
                .withQuery(QueryBuilders.multiMatchQuery("习近平","name", "content"))
                .build();
        org.springframework.data.elasticsearch.core.SearchHits<Article> search = template.search(query, Article.class);
//     TODO: 2021/1/5  template.search()
        return null;


    }
}
