package com.example.hospital.api.control.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UpdateDeptSubByIdForm {
    @NotNull(message = "id不能为空")
    @Min(value = 1,message = "id不能小于1")
    private Integer id;

    @NotNull(message = "deptId不能为空")
    @Min(value = 1,message = "deptId不能小于1")
    private Integer deptId;

    @NotBlank(message = "name不能为空")
    private  String name;

    @NotBlank(message = "location不能为空")
    private  String location;

}
