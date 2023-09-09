package com.bcyy.model.user.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * APP用户信息表
 * </p>
 *
 * @author itheima
 */
@Data
@TableName(value = "wx_user")
public class WxUser implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("name")
    private String name;
    @TableField("openid")
    private String openid;
    @TableField("phone")
    private String phone;
    @TableField("avatar")
    private String avatar;
    @TableField("gender")
    private Boolean gender;
    @TableField("email")
    private String email;
    @TableField("birthday")
    private String birthday;
    @TableField("degree")
    private String degree;
    @TableField("company_id")
    private String companyId;
    private String standing;
    private String information;
    /**
     * 0 未
            1 是
     */
//    @TableField("is_certification")
//    private Boolean certification;

    /**
     * 是否身份认证
     */
//    @TableField("is_identity_authentication")
//    private Boolean identityAuthentication;

    /**
     * 0正常
            1锁定
     */
//    @TableField("status")
//    private Boolean status;

    /**
     * 0 普通用户
            1 自媒体人
            2 大V
     */
//    @TableField("flag")
//    private Short flag;

}