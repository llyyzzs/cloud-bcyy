package com.bcyy.search;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bcyy.model.item.pojos.HomeItem;
import com.bcyy.model.search.vos.ItemSearch;
import com.bcyy.search.mapper.SearchItemMapper;
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
    SearchItemMapper searchItemMapper;

    @Autowired
    private RestHighLevelClient client;
    /**
     * 注意：数据量的导入，如果数据量过大，需要分页导入
     * @throws Exception
     */
    @Test
    public void testAddDocument() throws IOException {

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
        client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }
}
