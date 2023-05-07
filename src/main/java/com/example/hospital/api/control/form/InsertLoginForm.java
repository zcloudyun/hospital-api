package com.example.hospital.api.control.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class InsertLoginForm {
    @NotBlank(message = "nickname不能为空")
    private String nickname;

    @NotBlank(message = "password不能为空")
    private String password;
}
