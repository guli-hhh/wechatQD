package com.obtk.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
@Data
@ToString
public class Projects implements Serializable {
    private Integer pro_id;
    private Integer userId;
    private String projectName;
    private long startTime;
    private long endTime;
    private String place_name;
    private String place_address;
    private double latitude;
    private double longitude;
    private Integer qrcode;
    private String add;
    private Integer inviteId;
    private Integer status;
    private String diy1;
    private String diy2;
    private String diy3;
    private String diy4;
    private String diy5;
    private Integer enroll;
    private Integer finish;
    private long createTime;
}
