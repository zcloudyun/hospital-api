package com.example.hospital.api.control.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DeleteByIdForm {
    @NotEmpty(message = "ids不能为空")
    private Integer[] ids;
}
