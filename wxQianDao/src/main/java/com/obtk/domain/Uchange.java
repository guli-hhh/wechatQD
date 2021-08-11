package com.obtk.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
@Data
@ToString
public class Uchange implements Serializable {
    private Integer userId;
    private String openId;
    private String session_key;
}
