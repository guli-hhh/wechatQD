package com.obtk.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;
@Data
@ToString
public class Advert implements Serializable {
    private Integer ad_id;
    private String advertName;
    private String advertType;
    private String advertUrl;
    private String status;
    private Date onlineTime;
    private Date underTime;
    private Integer showNum;
    private Integer clickNum;
    private String spare1;
    private String spare2;


}
