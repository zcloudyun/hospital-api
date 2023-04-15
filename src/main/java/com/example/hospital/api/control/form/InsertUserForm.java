package com.example.hospital.api.control.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Data
public class InsertUserForm {
    @NotBlank(message = "username不能为空")
    private  String username;

    @NotBlank(message = "password不能为空")
    private  String password;

    @NotBlank(message = "name不能为空")
    private  String name;

    @NotBlank(message = "sex不能为空")
    @Pattern(regexp ="^男$|^女$" ,message = "sex内容不正确")
    private String sex;

    @NotBlank(message = "tel不能为空")
    @Pattern(regexp = "^1[1-9][0-9]{9}$",message = "tel内容不正确")
    private String tel;

    @NotBlank(message = "email不能为空")
    @Email
    @Length(max=200 ,message = "email内容不正确")
    private String email;

    @NotBlank(message = "job不能为空")
    @Pattern(regexp = "^主治医师$|^副主治医师$|^主任医师$|^副主任医师$",message = "job内容不正确")
    private String job;

    @NotNull(message = "deptId不能为空")
    @Min(value = 1,message = "deptId不能小于1")
    private Integer deptId;

    @NotNull(message = "status不能为空")
    @Range(min=1,max=3,message = "status不能为空")
    private Byte status;

}
