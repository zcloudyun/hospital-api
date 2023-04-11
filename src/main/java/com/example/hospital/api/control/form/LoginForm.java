package com.example.hospital.api.control.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
//验证接收到的内容
@Data
public class LoginForm {
    //验证字符串不为null也不为空字符串,括号里的内容是字符串验证错误返回的结果
    @NotBlank(message = "username不能为空")
    //验证字符串的表达式，第二个参数为验证错误返回的结果
    @Pattern(regexp="^[a-zA-Z0-9]{5,50}$",message = "username内容不正确")
    private String username;
    @NotBlank(message = "password不能为空")
    @Pattern(regexp="^[a-zA-Z0-9]{6,20}$",message = "password内容不正确")
    private String password;
}
