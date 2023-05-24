package com.example.hospital.api.control.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class SearchDoctorByDeptSubIdForm {
  private Integer deptSubId;
}
