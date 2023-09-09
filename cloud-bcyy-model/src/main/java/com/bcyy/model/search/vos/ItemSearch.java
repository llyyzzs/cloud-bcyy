package com.bcyy.model.search.vos;

import com.bcyy.model.item.dtos.Address;
import com.bcyy.model.item.dtos.Address2;
import com.bcyy.model.item.pojos.HomeItem;
import com.bcyy.model.item.vos.ItemCompanyDvo;
import com.bcyy.model.item.vos.ItemCompanyDvo2;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemSearch {
    private String  id;
    private String name;
    private String price;
    private String tags;
    private List<String> label;
    private String image;
    private String hRname;
    private Integer type;
    private ItemCompanyDvo2 itemCompanyDvo;
    // 排序时的 距离值
    private Object distance;
    //建议
    private List<String> suggestion;
    public ItemSearch(HomeItem homeItem){
        this.id=homeItem.getItemId();
        this.name=homeItem.getName();
        this.price=homeItem.getPrice();
        this.tags=homeItem.getTags();
        this.label=homeItem.getLabel();
        this.image=homeItem.getImage();
        this.hRname=homeItem.getHRname();
        this.type=homeItem.getType();
        ItemCompanyDvo2 itemCompanyDvo2 = new ItemCompanyDvo2();
        ItemCompanyDvo itemCompanyDvo1 = homeItem.getItemCompanyDvo();
        BeanUtils.copyProperties(itemCompanyDvo1,itemCompanyDvo2);
        Address address = homeItem.getItemCompanyDvo().getAddress();
        Address2 address2 = new Address2();
        BeanUtils.copyProperties(address,address2);
        address2.setPosition(address.getPosition().getLongitude()+", "+address.getPosition().getLatitude());
        itemCompanyDvo2.setAddress(address2);
        this.itemCompanyDvo=itemCompanyDvo2;
        // 组装suggestion
        this.suggestion = Arrays.asList(this.name,this.itemCompanyDvo.getName(),this.itemCompanyDvo.getAddress().getCity());
    }
}
