package com.example.hospital.api.control.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
public class UpdateMedicineForm {
    @NotNull(message = "id不能为空")
    @Min(value = 1,message = "id不能小于1")
    private Integer id;

    @NotBlank(message = "medicine_name不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9f5a]{2,20}$",message = "medicine_name内容不正确")
    private  String medicine_name;

    @NotBlank(message = "scale不能为空")
    private String scale;

    @NotBlank(message = "approval_num不能为空")
    private String approval_num;

    @NotBlank(message = "effects不能为空")
    private String effects;

    @NotBlank(message = "use不能为空")
    private String use;

    @NotBlank(message = "bzq不能为空")
    private String bzq;

    @NotBlank(message = "price不能为空")
    private String price;

    @NotNull(message = "num不能为空")
    private Integer num;
}
