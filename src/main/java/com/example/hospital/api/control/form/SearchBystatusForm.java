package com.example.hospital.api.control.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class SearchBystatusForm {
    @NotNull(message = "status不能为空")
    @Min(value=0,message = "status不能小于0")
    private Integer status;
    public int userId;
}
