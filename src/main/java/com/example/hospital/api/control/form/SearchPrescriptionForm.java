package com.example.hospital.api.control.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class SearchPrescriptionForm {
    @Pattern(regexp="^[\\u4e00-\\u9fa5]{1,20}$",message = "name内容不正确")
    private String name;

    @Pattern(regexp="^[\\u4e00-\\u9fa5]{1,20}$",message = "doctor_name内容不正确")
    private String doctor_name;

//    @NotBlank(message = "date不能为空")
    @Pattern(regexp = "^((?!0000)[0-9]{4}-((0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-8])|(0[13-9]|1[0-2])-(29|30)|(0[13578]|1[02])-31)|([0-9]{2}(0[48]|[2468][048]|[13579][26])|(0[48]|[2468][048]|[13579][26])00)-02-29)$",message = "date内容不正确")
    private String date;

    @Min(value=1,message = "dept_id不能小于1")
    private Integer dept_id;

    @Min(value = 1,message = "page不能小于1")
    private Integer page;
    @NotNull(message = "length不能为空")

    @Range(min=10,max=50,message = "length内容不正确")
    private Integer length;
}
