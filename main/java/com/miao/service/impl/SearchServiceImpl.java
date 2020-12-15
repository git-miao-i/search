package com.miao.service.impl;


import com.miao.service.SearchService;
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

import java.io.IOException;
import java.util.List;

/**
 * @author MiaoChuanXin
 */
public class SearchServiceImpl implements SearchService {
    @Autowired
    private RestHighLevelClient client;
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
        HighlightBuilder.Field title = new HighlightBuilder.Field("title");

        title.highlighterType("unified");
        highLight.field(title);
        builder.highlighter(highLight);

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



        request.source(builder);
        return null;
    }
}
