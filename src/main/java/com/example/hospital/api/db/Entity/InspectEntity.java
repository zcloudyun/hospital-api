package com.example.hospital.api.db.Entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InspectEntity {
    private Integer id;
    private String name;
    private Integer dept_id;
    private BigDecimal price;
    private Integer status;
    private String remark;
    private String createTime;
}
