package com.obtk.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Backstageuser implements Serializable {
    private Integer bu_id;
    private String userName;
    private String password;
}
