package com.obtk.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class UserInfo implements Serializable {
    private Integer userId;
    private String nickName;
    private String avatarUrl;
    private String city;
    private String country;
    private Integer gender;
    private String language;
    private String province;
    private Date warrantTime;
    private String warrantStr;
    private Date recentlyTime;
    private String recentlyStr;
    private Integer loginNum;
    private String spare1; //是否为禁用用户
    private String spare2;  //是否为标星用户
}
