package com.bcyy.model.search.vos;

import lombok.Data;

import java.util.List;
@Data
public class PageResult {
    private Long total;
    private List<ItemSearch> ItemSearchList;

    public PageResult() {
    }

    public PageResult(Long total, List<ItemSearch> ItemSearchList) {
        this.total = total;
        this.ItemSearchList = ItemSearchList;
    }
}