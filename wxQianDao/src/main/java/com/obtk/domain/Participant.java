package com.obtk.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;

@Data
@ToString
public class Participant implements Serializable {
    private Integer pId;
    private Integer userId;
    private Integer pro_id;
    private String pname;
    private String phone;
    private String diy3;
    private String diy4;
    private String diy5;
    private Integer status;
    private Date createTime;
    private Projects projects;
}
