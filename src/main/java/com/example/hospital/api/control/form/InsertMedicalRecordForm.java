package com.example.hospital.api.control.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class InsertMedicalRecordForm {
    @NotBlank(message = "record_code不能为空")
    private String record_code;

    @NotNull(message = "user_id不能为空")
    @Min(value = 1,message = "user_id不能小于1")
    private Integer user_id;

    @NotNull(message = "doctor_id不能为空")
    @Min(value = 1,message = "doctor_id不能小于1")
    private Integer doctor_id;

    @NotNull(message = "dept_id不能为空")
    @Min(value = 1,message = "dept_id不能小于1")
    private Integer dept_id;

    @NotNull(message = "registion_id不能为空")
    @Min(value = 1,message = "registion_id不能小于1")
    private Integer registion_id;

    @NotBlank(message = "date不能为空")
    private String date;

    @NotBlank(message = "description不能为空")
    private String description;

    @NotBlank(message = "checkinfo不能为空")
    private String checkinfo;

    @NotBlank(message = "total_price不能为空")
    private String total_price;

    @NotBlank(message = "diagnosis不能为空")
    private String diagnosis;

    private String[] disease_history;
    private String prescription;
    private String comment;
    private String remark;

    @NotBlank(message = "diagnostictype不能为空")
    private String diagnostictype;

    private String insurance_type;

    @NotNull(message = "status不能为空")
    @Min(value = 1,message = "status不能小于1")
    private Integer status;
}
