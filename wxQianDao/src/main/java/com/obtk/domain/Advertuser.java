package com.obtk.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;

@Data
@ToString
public class Advertuser implements Serializable {
    private Integer au_id;
    private Integer userId;
    private Integer ad_id;
    private Date clickTime;
    private UserInfo userInfo;

}
