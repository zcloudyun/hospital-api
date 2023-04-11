package com.example.hospital.api.control.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Data
public class InsertMedicineForm {
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
