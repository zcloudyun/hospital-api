package com.example.hospital.api.control.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Data
public class InsertWorkPlanForm {
    @NotNull(message = "deptSubId不能为空")
    @Min(value = 1,message = "deptSubId不能小于1")
    private Integer deptSubId;

    @NotNull(message = "doctorId不能为空")
    @Min(value = 1,message = "doctorId不能小于1")
    private Integer doctorId;

    @NotBlank(message = "date不能为空")
    @Pattern(regexp = "^((?!0000)[0-9]{4}-((0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-8])|(0[13-9]|1[0-2])-(29|30)|(0[13578]|1[02])-31)|([0-9]{2}(0[48]|[2468][048]|[13579][26])|(0[48]|[2468][048]|[13579][26])00)-02-29)$",message = "date内容不正确")
    private String date;

    @NotNull(message = "totalMaximum不能为空")
    @Range(min=1,max=150,message = "totalMaximum内容不正确")
    private Integer totalMaximum;

    @NotNull(message = "slotMaximum不能为空")
    @Range(min=1,max=10,message = "slotMaximum内容不正确")
    private Integer slotMaximum;

    @NotEmpty(message = "slots不能为空")
    private Integer[] slots;
}