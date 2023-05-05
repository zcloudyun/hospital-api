package com.example.hospital.api.control.form;

import cn.hutool.core.date.DateTime;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InsertInspectRecordForm {
    private int hitId;
    private int patientCardId;
    private DateTime date;
    private int doctorId;
    private String remark;
    private BigDecimal price;
}
