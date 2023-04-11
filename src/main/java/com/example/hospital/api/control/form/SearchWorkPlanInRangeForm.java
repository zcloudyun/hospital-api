package com.example.hospital.api.control.form;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SearchWorkPlanInRangeForm {
 @NotBlank(message = "startData不能为空")
 @Pattern(regexp = "^((?!0000)[0-9]{4}-((0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-8])|(0[13-9]|1[0-2])-(29|30)|(0[13578]|1[02])-31)|([0-9]{2}(0[48]|[2468][048]|[13579][26])|(0[48]|[2468][048]|[13579][26])00)-02-29)$",message = "endDate内容不正确")
 private String startDate;

 @NotBlank(message = "endDate不能为空")
 @Pattern(regexp = "^((?!0000)[0-9]{4}-((0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-8])|(0[13-9]|1[0-2])-(29|30)|(0[13578]|1[02])-31)|([0-9]{2}(0[48]|[2468][048]|[13579][26])|(0[48]|[2468][048]|[13579][26])00)-02-29)$",message = "endDate内容不正确")
 private String endDate;

 @Min(value = 1,message = "deptId不能为空")
 private Integer deptId;

 @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]{1,20}$",message = "doctorName内容不能为空")
 private String doctorName;
}
