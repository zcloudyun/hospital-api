package com.example.hospital.api.control.form.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class DoctorScheduleSlotVO {
    @Min(value = 1,message = "scheduleId不能小于1")
    private Integer scheduleId;

    @NotNull(message = "slot不能为空")
    @Range(min=1,max=16,message = "slot内容不正确")
    private Integer slot;

    @NotNull(message = "maximum不能为空")
    @Range(min=1,max = 10,message = "maximum内容不正确")
    private Integer maximum;

    @NotNull(message = "operate不能为空")
    @Pattern(regexp = "^insert$|^delete$",message = "operate内容不正确")
    private String operate;
}
