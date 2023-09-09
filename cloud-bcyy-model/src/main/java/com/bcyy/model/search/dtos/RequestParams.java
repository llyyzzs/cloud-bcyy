package com.bcyy.model.search.dtos;

import com.bcyy.model.item.dtos.Position;
import lombok.Data;

@Data
public class RequestParams {
    private String key;
    private Integer page;
    private Integer size;
    private String sortBy;
    // 下面是新增的过滤条件参数
    private String city;
    private String tags;
    private String type;
    private Position position;

}
