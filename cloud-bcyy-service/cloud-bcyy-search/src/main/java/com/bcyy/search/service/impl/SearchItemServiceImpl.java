package com.bcyy.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.item.dtos.Position;
import com.bcyy.model.item.pojos.HomeItem;
import com.bcyy.model.search.dtos.RequestParams;
import com.bcyy.model.search.vos.ItemSearch;
import com.bcyy.search.mapper.SearchItemMapper;
import com.bcyy.search.service.SearchItemService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@Transactional
public class SearchItemServiceImpl implements SearchItemService {
    @Autowired
    RestHighLevelClient client;
    @Autowired
    SearchItemMapper searchItemMapper;
    /**
     * 搜索
     */
    @Override
    public ResponseResult search(RequestParams params) {
        try {
            SearchRequest searchRequest = new SearchRequest("bcyy_item");
            buildBasicQuery(params,searchRequest);
            if (params.getPage() == null) {
                params.setPage(1);
            }
            if (params.getSize() == null){
                params.setSize(10);
            }
            searchRequest.source().size(params.getSize()).from((params.getPage()-1)*params.getSize());
            // 位置排序
            if (params.getPosition() != null) {
                String location = params.getPosition().getLatitude() + ", " + params.getPosition().getLongitude();
                if (location != null && !location.equals("")) {
                    searchRequest.source().sort(SortBuilders
                            .geoDistanceSort("itemCompanyDvo.address.position", new GeoPoint(location))
                            .order(SortOrder.ASC)
                            .unit(DistanceUnit.KILOMETERS)
                    );
                }
            }
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            List<ItemSearch> homeItemList = handleResponse(response);
            return ResponseResult.okResult(homeItemList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //聚合标签
    public ResponseResult aggTags(String name){
        try {
            SearchRequest searchRequest = new SearchRequest("bcyy_item");
            searchRequest.source().size(0);
            searchRequest.source().aggregation(AggregationBuilders
                    .terms("tags_agg")
                    .field(name)
                    .size(20));
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            //解析聚合结果
            Aggregations aggregations = response.getAggregations();
            Terms tags_agg = aggregations.get("tags_agg");
            List<? extends Terms.Bucket> buckets = tags_agg.getBuckets();
            //遍历
            HashSet<String> hashSet = new HashSet<>();
            for (Terms.Bucket bucket:buckets) {
                String keyAsString = bucket.getKeyAsString();
                hashSet.add(keyAsString);
            }
            return ResponseResult.okResult(hashSet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //搜索建议
    public ResponseResult getSuggestions(String prefix) {
        try {
            // 1.准备Request
            SearchRequest request = new SearchRequest("bcyy_item");
            // 2.准备DSL
            request.source().suggest(new SuggestBuilder().addSuggestion(
                    "suggestions",
                    SuggestBuilders.completionSuggestion("suggestion")
                            .prefix(prefix)
                            .skipDuplicates(true)
                            .size(10)
            ));
            // 3.发起请求
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            // 4.解析结果
            Suggest suggest = response.getSuggest();
            // 4.1.根据补全查询名称，获取补全结果
            CompletionSuggestion suggestions = suggest.getSuggestion("suggestions");
            // 4.2.获取options
            List<CompletionSuggestion.Entry.Option> options = suggestions.getOptions();
            // 4.3.遍历
            List<String> list = new ArrayList<>(options.size());
            for (CompletionSuggestion.Entry.Option option : options) {
                String text = option.getText().toString();
                list.add(text);
            }
            return ResponseResult.okResult(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /*
    * 查询附近
    * */
    public ResponseResult searchPosition(Position position) throws IOException {
        SearchRequest searchRequest = new SearchRequest("bcyy_item");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 构建地理位置查询条件
        searchSourceBuilder.query(QueryBuilders.geoDistanceQuery("itemCompanyDvo.address.position")
                .point(position.getLatitude(), position.getLongitude())
                .distance("15km"));

        searchRequest.source(searchSourceBuilder);

        // 执行查询
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        List<ItemSearch> homeItemList = handleResponse(searchResponse);
        return ResponseResult.okResult(homeItemList);
    }
    //复合查询
    public void buildBasicQuery(RequestParams params, SearchRequest request) {
        // 1.构建BooleanQuery
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 2.关键字搜索
        String key = params.getKey();
        if (key == null || "".equals(key)) {
            boolQuery.must(QueryBuilders.matchAllQuery());
        } else {
            boolQuery.must(QueryBuilders.matchQuery("all", key));
        }
        // 3.城市条件
        if (params.getCity() != null && !params.getCity().equals("")) {
            boolQuery.filter(QueryBuilders.matchQuery("itemCompanyDvo.address.city", params.getCity()));
        }
        // 4.标签条件
        if (params.getTags() != null && !params.getTags().equals("")) {
            boolQuery.filter(QueryBuilders.matchQuery("tags", params.getTags()));
        }
        // 5.类型条件
        if (params.getType() != null && !params.getType().equals("")) {
            boolQuery.filter(QueryBuilders.termQuery("type", params.getType()));
        }
        // 7.放入source
        request.source().query(boolQuery);
    }

    //解析结果
    public List<ItemSearch> handleResponse(SearchResponse response){
        List<ItemSearch> list = new ArrayList<>();
        //解析响应
        SearchHits hits = response.getHits();
        //总条数
        long total = hits.getTotalHits().value;
        //文档数组
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit hit:hits1) {
            String sourceAsString = hit.getSourceAsString();
            ItemSearch itemSearch = JSON.parseObject(sourceAsString, ItemSearch.class);
            Object[] sortValues = hit.getSortValues();
            if (sortValues.length > 0) {
                Object sortValue = sortValues[0];
                itemSearch.setDistance(sortValue+"千米");
            }
            list.add(itemSearch);
        }
        return list;
    }
    //添加文档
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "item.insert.queue"),
            exchange = @Exchange(value = "item.topic"),
            key = "item.insert"
    ))
    public void add(HomeItem homeItem){
        BulkRequest bulkRequest = new BulkRequest();
        // 2.转换为文档类型
        ItemSearch itemSearch = new ItemSearch(homeItem);
        // 3.将HotelDoc转json
        String json = JSON.toJSONString(itemSearch);
        // 1.准备Request对象
        IndexRequest request = new IndexRequest("bcyy_item").id(itemSearch.getId());
        // 2.准备Json文档
        request.source(json, XContentType.JSON);
        bulkRequest.add(request);
        // 3.发送请求
        try {
            client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //添加所有文档
    public ResponseResult addList(){
        List<HomeItem> homeItemList = searchItemMapper.selectList(
                new QueryWrapper<HomeItem>().eq("state",1));
        BulkRequest bulkRequest = new BulkRequest();
        // 2.转换为文档类型
        for (HomeItem homeItem :homeItemList) {
            ItemSearch itemSearch = new ItemSearch(homeItem);
            bulkRequest.add(new IndexRequest("bcyy_item")
                    .id(itemSearch.getId())
                    .source(JSON.toJSONString(itemSearch),XContentType.JSON));
        }
        // 3.发送请求
        try {
            client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 3.发送请求
        return ResponseResult.okResult(200,"添加成功");
    }

    //删除文档
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "item.delete.queue"),
            exchange = @Exchange(value = "item.topic"),
            key = "item.delete"
    ))
    public void delete(String id){
        // 1.准备Request
        DeleteRequest request = new DeleteRequest("bcyy_item", id);
        // 2.发送请求
        try {
            client.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        return ResponseResult.okResult(200,"删除成功");
    }

}
