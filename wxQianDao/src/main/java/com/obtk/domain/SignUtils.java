package com.obtk.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
@Data
@ToString
public class SignUtils implements Serializable {
    private Integer userId;
    private String placeName;
    private String placeAdress;
    private double lat;
    private double lng;
    private Integer num;
    private String markmsg;

}
