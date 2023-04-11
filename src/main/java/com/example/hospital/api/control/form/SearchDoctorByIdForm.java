package com.example.hospital.api.control.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Data
public class SearchDoctorByIdForm {
   @NotNull(message = "id不能为空")
   @Min(value = 1,message = "id不能小于1")
   private Integer id;

}
