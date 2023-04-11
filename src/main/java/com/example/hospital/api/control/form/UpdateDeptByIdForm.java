package com.example.hospital.api.control.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateDeptByIdForm {
    @NotNull(message = "id不能为空")
    @Min(value = 1,message = "id不能小于1")
    private Integer id;

    @NotBlank(message = "name不能为空")
    private  String name;

    private  Boolean outpatient;

    @NotBlank(message = "description不能为空")
    private  String description;

    private  Boolean recommended;
}
