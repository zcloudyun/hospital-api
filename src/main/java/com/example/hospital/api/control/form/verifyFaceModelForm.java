package com.example.hospital.api.control.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class verifyFaceModelForm {
    @NotBlank(message = "photo不能为空")
    private String photo;
}
