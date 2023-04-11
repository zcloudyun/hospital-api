package com.example.hospital.api.control.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateFaceModelForm {
    private Integer userId;

    @NotBlank(message = "photo不能为空")
    private String photo;
}
