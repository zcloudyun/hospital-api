package com.example.hospital.api.control.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class InsertDeptForm {
    @NotBlank(message = "name不能为空")
    private  String name;

    private  Boolean outpatient;

    @NotBlank(message = "description不能为空")
    private  String description;

    private  Boolean recommended;
}
