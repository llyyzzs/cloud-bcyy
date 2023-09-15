package com.bcyy.search;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bcyy.apis.item.DetailsItemApi;
import com.bcyy.model.item.dtos.HomeItemDto;
import com.bcyy.model.item.pojos.HomeItem;
import com.bcyy.model.search.vos.ItemSearch;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ItemSearchTest {
    @Autowired
    DetailsItemApi detailsItemApi;

    @Autowired
    private RestHighLevelClient client;
    /**
     * 注意：数据量的导入，如果数据量过大，需要分页导入
     * @throws Exception
     */
    @Test
    public void testAddDocument() throws IOException {

        Object data = detailsItemApi.getItem("280aed7d-2f69-411b-a51b-88465733ab33").getData();
        String s = JSON.toJSONString(data);
        HomeItem homeItem = JSON.parseObject(s, HomeItem.class);
        BulkRequest bulkRequest = new BulkRequest();
        // 2.转换为文档类型
        ItemSearch itemSearch = new ItemSearch(homeItem);
        bulkRequest.add(new IndexRequest("bcyy_item")
                .id(itemSearch.getId())
                .source(JSON.toJSONString(itemSearch),XContentType.JSON));
        // 3.发送请求
        client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }
}
