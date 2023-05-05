package com.example.hospital.api.control.form;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateInspectForm {
    private int id;
    private String name;
    private int dept_id;
    private BigDecimal price;
    private int status;
    private String remark;
}
