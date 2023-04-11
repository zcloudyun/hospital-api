package com.example.hospital.api.control.form;

import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UpdatePriceByIdForm {
    @NotNull(message = "doctorId不能为空")
    @Min(value = 1,message = "doctorId不能小于1")
    private Integer doctorId;

    @NotNull(message = "price_1不能为空")
    @Pattern(regexp = "^[1-9]\\d*\\.\\d{1,2}$|^0\\.\\d{1,2}$|^[1-9]\\d*$", message="金额格式错误")
    private String price_1;

    @NotNull(message = "price_2不能为空")
    @Pattern(regexp = "^[1-9]\\d*\\.\\d{1,2}$|^0\\.\\d{1,2}$|^[1-9]\\d*$", message="金额格式错误")
    private String price_2;


}
