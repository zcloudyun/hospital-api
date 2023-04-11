package com.example.hospital.api.control.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DeleteMedicalRecordForm {
    @NotEmpty(message = "ids不能为空")
    private Integer[] ids;
}
