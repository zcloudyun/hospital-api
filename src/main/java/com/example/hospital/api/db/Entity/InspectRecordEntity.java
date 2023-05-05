package com.example.hospital.api.db.Entity;

import cn.hutool.core.date.DateTime;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InspectRecordEntity {
    private Integer id;
    private Integer hitId;
    private Integer patientCardId;
    private String hirDate;
    private Integer status;
    private Integer doctorId;
    private String remark;
    private BigDecimal price;
    private DateTime date;
    private String createTime;
}
