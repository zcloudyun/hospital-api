package com.example.hospital.api.control.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class DeleteInspectImageForm {
    @NotNull(message = "recordId不能为空")
    @Min(value = 1,message = "recordId不能小于1")
    private int recordId;

    private String filename;
}
