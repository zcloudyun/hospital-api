package com.example.hospital.api.control.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
@Data
public class SearchMedicineByPageForm {
    @Pattern(regexp="^[\\u4e00-\\u9fa5]{1,20}$",message = "medicine_name")
    private String medicine_name;

    @NotNull(message = "page不能为空")
    @Min(value = 1,message = "page不能小于1")
    private Integer page;
    @NotNull(message = "length不能为空")
    @Range(min=10,max=50,message = "length内容不正确")
    private Integer length;
}
